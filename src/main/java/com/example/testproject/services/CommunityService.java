package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.custom.CommunityNotFoundException;
import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.models.Dto.CommunityDto;
import com.example.testproject.models.models.requests.CommunityRequest;
import com.example.testproject.repositories.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final RoleSystemService roleSystemService;
    private final ImageService imageService;

    public Community findById(Long id){
        return communityRepository.findById(id).orElseThrow(CommunityNotFoundException::new);
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
