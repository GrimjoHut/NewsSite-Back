package com.example.testproject.models.models.Dto;

import com.example.testproject.models.entities.Community;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityDto {
    private ImageDto avatar;
    private String name;
    private String description;

    public static CommunityDtoBuilder basicMapping(Community community){
        return CommunityDto
                .builder()
                .avatar(ImageDto.mapFromEntity(community.getAvatar()))
                .name(community.getName());
    }

    public static CommunityDto mapFromEntitySimplified(Community community){
        return basicMapping(community).build();
    }

    public static CommunityDto mapFromEntity(Community community){
        return basicMapping(community)
                .description(community.getDescription())
                .build();
    }
}
