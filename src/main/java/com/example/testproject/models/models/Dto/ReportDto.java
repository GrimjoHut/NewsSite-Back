package com.example.testproject.models.models.Dto;

import com.example.testproject.models.entities.Report;
import com.example.testproject.models.enums.ReportTypeEnum;
import com.example.testproject.utils.Formatter;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ReportDto {
    private String description;
    private String createDate;
    private UserDto reporter;
    private ReportTypeEnum reportType;

    public static ReportDtoBuilder basicMapping(Report report){
        return ReportDto.builder()
                .reportType(report.getType())
                .createDate(report.getCreateDate().format(Formatter.formatter))
                .reporter(UserDto.mapFromEntitySimplify(report.getUser()));
    }

    public static ReportDto mapFromEntity(Report report){
        return basicMapping(report)
                .description(report.getDescription())
                .build();
    }
}
