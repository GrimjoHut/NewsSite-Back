package com.example.testproject.configs;


import com.example.testproject.Security.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
    };

    private static final String[] EVERYBODY_URLS = {
            "/login",
            "/registration",
            "/commentariesToPost/{id}",
            "/posts",
            "/post/{id}",
            "/images",
            "/send",
            "/verify",
            "/verify/**"
    };

    private static final String[] ADMIN_URLS = {
            "/giveRole",
            "/takeRole"
    };

    private static final String[] USER_URLS = {
            "/createComment",
            "/changeComment/{id}",
            "/deleteComment",
            "/delete_post",
            "/like_post",
            "/dislike_post",
            "/newRequest",
    };

    private static final String[] MODER_URLS = {
            "/new_post",
            "/accept_request/{id}",
            "/reject_request/{id}",
            "/request/{id}",
            "/requests",
    };


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(EVERYBODY_URLS).permitAll();
                    auth.requestMatchers(WHITE_LIST_URL).permitAll();
                    auth.requestMatchers(MODER_URLS).hasRole("MODER");
                    auth.requestMatchers(ADMIN_URLS).hasRole("ADMIN");
                    auth.requestMatchers(USER_URLS).hasRole("USER");
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
