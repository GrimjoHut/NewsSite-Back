package com.example.testproject.controllers;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.models.entities.Report;
import com.example.testproject.models.models.Dto.ReportDto;
import com.example.testproject.models.models.requests.ReportRequest;
import com.example.testproject.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @Secured("ROLE_USER")
    @PostMapping("/createRepost")
    public ResponseEntity<Report> createReport(@RequestBody ReportRequest request,
                                               @AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportService.createReport(request, userDetails));
    }

    @Secured("ROLE_MODER")
    @GetMapping("/reportsToPost")
    public ResponseEntity<Page<ReportDto>> reportsToPost(@RequestParam Long postId,
                                                         @PageableDefault Pageable pageable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportService.reportsToPost(postId, pageable)
                        .map(ReportDto::mapFromEntity));
    }

    @Secured("ROLE_MODER")
    @PutMapping("/considerReport")
    public ResponseEntity<String> considerReport(@RequestParam Long reportId){
        reportService.considerReport(reportId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Report considered");
    }
}
