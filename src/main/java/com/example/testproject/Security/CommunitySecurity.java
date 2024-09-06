package com.example.testproject.Security;

import com.example.testproject.models.entities.Community;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommunitySecurity {

    private final CommunityService communityService;

    public boolean hasRoleInCommunity(Long communityId, String role) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Community community = communityService.findById(communityId);
        if (community == null) {
            return false;
        }

        Set<CommunityRoleEnum> roles = userDetails.getCommunityAuthorities(community);
        return roles.stream().anyMatch(r -> r.name().equals(role));
    }
}
