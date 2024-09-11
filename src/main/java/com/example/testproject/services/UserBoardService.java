package com.example.testproject.services;

import com.example.testproject.exceptions.custom.UserBoardNotFoundException;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.UserBoard;
import com.example.testproject.repositories.UserBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBoardService {

    private final UserBoardRepository userBoardRepository;


    public Optional<UserBoard> findByPost(Post post){
        return userBoardRepository.findByPostsContaining(post);
    }

    public UserBoard findById(Long id){
        return userBoardRepository.findById(id)
                .orElseThrow(UserBoardNotFoundException::new);
    }
}
