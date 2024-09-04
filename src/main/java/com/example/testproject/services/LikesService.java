package com.example.testproject.services;

import com.example.testproject.models.entities.Likes;
import com.example.testproject.models.enums.LikesEnum;
import com.example.testproject.models.models.responses.LikeJPQLRes;
import com.example.testproject.repositories.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    public LikeJPQLRes likesPost(Long postId, Long commentId) {
        LikeJPQLRes likeJPQLRes = likesRepository.countLikesAndDislikes(postId, commentId);
        return likeJPQLRes;
    }

    public void likePost(UUID postId, LikesEnum reaction){

    }
}
