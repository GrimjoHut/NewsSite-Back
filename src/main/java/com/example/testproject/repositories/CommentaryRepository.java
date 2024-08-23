package com.example.testproject.repositories;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.CommentaryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Integer> {

    Commentary findTopByPostOrderByCreatedAtDesc(Post post);

    List<Commentary> findByPostOrderByCreatedAtDesc(Pageable pageable, Post post);
}