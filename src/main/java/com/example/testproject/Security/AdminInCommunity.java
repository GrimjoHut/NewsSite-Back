package com.example.testproject.Security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("@communitySecurity.hasRoleInCommunity(#communityId, 'ROLE_ADMIN')")
public @interface AdminInCommunity {
}
