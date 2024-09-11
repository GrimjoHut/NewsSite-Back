package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.enums.FriendStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final CommunityService communityService;

    public Boolean isCommunityAdmin(CustomUserDetails userDetails, Long communityId){
        Community community = communityService.findById(communityId);
        return (userDetails.getUser()
                .getRoleSystems().stream()
                .filter(roleSystem -> roleSystem.getCommunity().equals(community))
                .flatMap(roleSystem -> roleSystem.getRoles().stream())
                .toList().contains(CommunityRoleEnum.ROLE_ADMIN));
    }

    public Boolean isSubscriber(CustomUserDetails userDetails, Long communityId){
        Community community = communityService.findById(communityId);
        return (userDetails.getUser()
                .getRoleSystems().stream()
                .filter(roleSystem -> roleSystem.getCommunity().equals(community))
                .flatMap(roleSystem -> roleSystem.getRoles().stream())
                .toList().contains(CommunityRoleEnum.ROLE_SUBSCRIBER));
    }

    public Boolean isFriend(CustomUserDetails userDetails, User user){
        return  user
                .getFriends()
                .stream()
                .anyMatch(friend ->
                    friend.getFriend().getId().equals(userDetails.getUser().getId()) &&
                        friend.getStatus() == FriendStatusEnum.FRIEND);
    }
}
