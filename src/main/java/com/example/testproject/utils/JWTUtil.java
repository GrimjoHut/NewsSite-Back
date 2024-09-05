package com.example.testproject.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

public class JWTUtil {
    private static final String SECRET_KEY = "If you’re not a socialist when you’re 25, you have no heart. If you’re not a conservative by the time you’re 35, you have no brain.";

    // Метод для генерации токена на основе имени пользователя
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000)) // 1 hour expiration
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY.getBytes())
                .compact();
    }

    // Извлечение имени пользователя из токена
    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Проверка валидности токена
    public static boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (ExpiredJwtException ex) {
            System.out.println("JWT token expired");
        }
        return false;
    }

    // Проверка валидности токена с учётом UserDetails
    public static boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Метод для проверки истечения срока действия токена
    public static boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(SECRET_KEY.getBytes())
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
