package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.custom.CommunityNotFoundException;
import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.entities.RoleSystem;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.models.Dto.CommunityDto;
import com.example.testproject.models.models.requests.CommunityRequest;
import com.example.testproject.repositories.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final UserService userService;
    private final RoleSystemService roleSystemService;
    private final ImageService imageService;

    public Community findById(Long id){
        return communityRepository.findById(id).orElseThrow(CommunityNotFoundException::new);
    }

    public void subscribeToCommunity(Long communityId, CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Community community = communityRepository.findById(communityId)
                .orElseThrow(CommunityNotFoundException::new);

        // Проверяем, является ли пользователь уже подписчиком
        if (community.getSubscribers().contains(user)) {
            throw new IllegalArgumentException("User is already subscribed to this community");
        }

        // Добавляем пользователя в подписчики коммьюнити
        community.getSubscribers().add(user);
        communityRepository.save(community);

        // Проверяем и обновляем запись в RoleSystem
        RoleSystem roleSystem = roleSystemService.findByUserAndCommunity(user, community);
        if (roleSystem == null) {
            roleSystem = new RoleSystem();
            roleSystem.setUser(user);
            roleSystem.setCommunity(community);
            roleSystem.setRoles(Set.of(CommunityRoleEnum.ROLE_SUBSCRIBER));
            roleSystemService.saveRoleSystem(roleSystem);
        } else {
            // Проверяем, есть ли уже роль подписчика
            if (!roleSystem.getRoles().contains(CommunityRoleEnum.ROLE_SUBSCRIBER)) {
                roleSystem.getRoles().add(CommunityRoleEnum.ROLE_SUBSCRIBER);
                roleSystemService.saveRoleSystem(roleSystem);
            }
        }
    }

    public void unsubscribeFromCommunity(Long communityId, CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        Community community = communityRepository.findById(communityId)
                .orElseThrow(CommunityNotFoundException::new);

        // Проверяем, является ли пользователь подписчиком
        if (!community.getSubscribers().contains(user)) {
            throw new IllegalArgumentException("User is not subscribed to this community");
        }

        // Удаляем пользователя из подписчиков коммьюнити
        community.getSubscribers().remove(user);
        communityRepository.save(community);

        // Проверяем и обновляем запись в RoleSystem
        RoleSystem roleSystem = roleSystemService.findByUserAndCommunity(user, community);
        if (roleSystem != null) {
            // Удаляем роль подписчика, если она есть
            roleSystem.getRoles().remove(CommunityRoleEnum.ROLE_SUBSCRIBER);

            // Если после удаления ролей роль система пуста, удаляем запись
            if (roleSystem.getRoles().isEmpty()) {
                roleSystemService.deleteRoleSystem(roleSystem);
            } else {
                roleSystemService.saveRoleSystem(roleSystem);
            }
        }
    }

    public Page<Community> findByUserId(Long userId, Pageable pageable){
        return communityRepository.findBySubscribersId(userId, pageable);
    }


    public Community createCommunity(CommunityRequest communityRequest,
                                     CustomUserDetails userDetails,
                                     MultipartFile file){
        Community community = new Community();
        community.setName(communityRequest.getName());
        community.setDescription(community.getDescription());
        community.getRoleSystems()
                .add(roleSystemService.createRoleSystem(community, userDetails.getUser(), CommunityRoleEnum.ROLE_CREATOR));
        try {
            Image image = imageService.uploadImage(file, "image-bucket");
            image.setCommunity(community);
            community.setAvatar(image);
        } catch (Exception e){
            e.printStackTrace();
        }
        communityRepository.save(community);
        return community;
    }
}
