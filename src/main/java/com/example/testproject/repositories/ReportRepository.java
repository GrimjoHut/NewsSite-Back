package com.example.testproject.repositories;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByPostAndConsideredFalseOrderByCreateDateDesc(Post post, Pageable pageable);
}

