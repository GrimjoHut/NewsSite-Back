package com.example.testproject.repositories;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.enums.StatusEnum;
import com.example.testproject.models.models.Dto.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    Page<Post> findByCommunityIdInAndStatusOrderByCreatedDateDesc(List<Long> communityIds, StatusEnum statusEnum, Pageable pageable);
}