package com.example.testproject.models.models.Dto;


import com.example.testproject.models.entities.Post;
import com.example.testproject.utils.Formatter;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PostDto {
    private String header;
    private String description;
    private UserDto author;
    private String createDate;
    private List<ImageDto> images;

    public static PostDtoBuilder basicMapping(Post post){
        return PostDto.builder()
                .author(UserDto.mapFromEntity(post.getUser()))
                .header(post.getHeader())
                .images(post.getImages().stream().map(ImageDto::mapFromEntity).collect(Collectors.toList()));
    }


    public static PostDto mapFromEntitySimplified(Post post){
        return basicMapping(post)
                .createDate(post.getCreatedDate().format(Formatter.formatter))
                .description(post.getDescription().length() < 400 ? post.getDescription() : post.getDescription().substring(0, 250) + "...")
                .build();
    }

    public static PostDto mapFromEntity(Post post){
        return basicMapping(post)
                .createDate(post.getCreatedDate().format(Formatter.formatter))
                .description(post.getDescription())
                .build();
    }
}
