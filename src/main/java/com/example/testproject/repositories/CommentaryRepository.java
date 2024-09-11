package com.example.testproject.repositories;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {

    Page<Commentary> findByPostOrderByCreatedDateDesc(Pageable pageable, Post post);

    Page<Commentary> findAll(Specification<Commentary> spec, Pageable pageable);
}