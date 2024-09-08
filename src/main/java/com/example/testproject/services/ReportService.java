package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.custom.ReportNotFoundException;
import com.example.testproject.models.entities.Report;
import com.example.testproject.models.models.requests.ReportRequest;
import com.example.testproject.repositories.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final PostService postService;

    public Report findById(Long reportId){
        return reportRepository.findById(reportId)
                .orElseThrow(ReportNotFoundException::new);
    }

    public Page<Report> reportsToPost(Long postId, Pageable pageable){
        Page<Report> reports = reportRepository.findByPostAndConsideredFalseOrderByCreateDateDesc(postService.findById(postId), pageable);
        return reports;
    }

    public void considerReport(Long reportId){
        Report report = this.findById(reportId);
        report.setConsidered(Boolean.TRUE);
        reportRepository.save(report);
    }

    public Report createReport(ReportRequest request, CustomUserDetails userDetails){
        Report report = new Report();
        report.setUser(userDetails.getUser());
        report.setPost(postService.findById(request.getPostId()));
        report.setCreateDate(OffsetDateTime.now());
        report.setDescription(request.getDescription());
        reportRepository.save(report);
        return report;
    }
}
