package com.example.testproject.models.models.Dto;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.utils.Formatter;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentaryDto {
    private String description;
    private UserDto author;
    private String createdDate;


    public static CommentaryDtoBuilder basicMapping(Commentary commentary){
        return CommentaryDto.builder()
                .author(UserDto.mapFromEntity(commentary.getUser()))
                .description(commentary.getDescription())
                .createdDate(commentary.getCreatedDate().format(Formatter.formatter));
    }

    public static CommentaryDto mapFromEntity(Commentary commentary){
        return basicMapping(commentary).build();
    }
}
