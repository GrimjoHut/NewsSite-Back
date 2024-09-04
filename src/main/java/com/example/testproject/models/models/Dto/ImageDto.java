package com.example.testproject.models.models.Dto;


import com.example.testproject.models.entities.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String url;

    public static ImageDtoBuilder basicMapping(Image image){
        return ImageDto.builder()
                .url(image.getFile().getPath());
    }

    public static ImageDto mapFromEntity(Image image){
        return basicMapping(image).build();
    }
}
