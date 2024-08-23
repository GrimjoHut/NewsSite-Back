package com.example.testproject.repositories;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.PostDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Post findById(int id);
}