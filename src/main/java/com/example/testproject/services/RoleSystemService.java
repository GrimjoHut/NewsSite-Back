package com.example.testproject.services;

import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.RoleSystem;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.repositories.RoleSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleSystemService {

    private final RoleSystemRepository roleSystemRepository;

    public void saveRoleSystem(RoleSystem roleSystem){
        roleSystemRepository.save(roleSystem);
    }

    public void deleteRoleSystem(RoleSystem roleSystem){
        roleSystemRepository.delete(roleSystem);
    }

    public RoleSystem findByUserAndCommunity(User user, Community community){
        return roleSystemRepository.findByUserAndCommunity(user, community);
    }

    public RoleSystem createRoleSystem(Community community, User user, CommunityRoleEnum communityRoleEnum){
        RoleSystem roleSystem = new RoleSystem();
        roleSystem.getRoles().add(communityRoleEnum);
        roleSystem.setUser(user);
        roleSystem.setCommunity(community);
        roleSystemRepository.save(roleSystem);
        return roleSystem;
    }

}
