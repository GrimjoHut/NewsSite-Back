package com.example.testproject.repositories;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.UserBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {
    Optional<UserBoard> findById(Long id);

    Optional<UserBoard> findByPostsContaining(Post post);
}
