package com.example.testproject.models.models.requests;

import com.example.testproject.models.enums.ReportTypeEnum;
import lombok.Data;

@Data
public class ReportRequest {
    private Long postId;
    private ReportTypeEnum type;
    private String description;
}
