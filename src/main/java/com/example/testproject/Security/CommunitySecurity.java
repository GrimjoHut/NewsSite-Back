package com.example.testproject.Security;

import com.example.testproject.models.entities.Community;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.services.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component("communitySecurity")
@RequiredArgsConstructor
public class CommunitySecurity {

    private final CommunityService communityService;

    public boolean hasRoleInCommunity(Long communityId, CustomUserDetails userDetails) {
        if (communityId == null) return false;

        Community community = new Community();
        community.setId(communityId);

        return userDetails.getCommunityAuthorities(community).contains(CommunityRoleEnum.ROLE_ADMIN);
    }
}
