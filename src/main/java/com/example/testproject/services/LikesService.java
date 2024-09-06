package com.example.testproject.services;

import com.example.testproject.exceptions.custom.LikeSearchInvalidParametrs;
import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Likes;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.LikesEnum;
import com.example.testproject.models.models.responses.LikeJPQLRes;
import com.example.testproject.repositories.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final PostService postService;
    private final UserService userService;
    private final CommentaryService commentaryService;

    public LikeJPQLRes likesPost(Long postId, Long commentId) {
        LikeJPQLRes likeJPQLRes = likesRepository.countLikesAndDislikes(postId, commentId);
        return likeJPQLRes;
    }

    public void like(Long postId, Long commId, LikesEnum reaction, Long userId) {
        if ((postId != null && commId != null) || (postId == null && commId == null))
            throw new LikeSearchInvalidParametrs();

        User user = userService.findById(userId);

        Likes like = null;
        if (postId != null) {
            Post post = postService.findById(postId);
            like = likesRepository.findByPostAndUser(post, user)
                    .orElse(new Likes());
            like.setPost(post);
        } else if (commId != null) {
            Commentary commentary = commentaryService.findById(commId);
            like = likesRepository.findByCommentAndUser(commentary, user)
                    .orElse(new Likes());
            like.setComment(commentary);
        }

        like.setUser(user);
        like.setReaction(reaction);

        likesRepository.save(like);  // Сохраняем или обновляем лайк
    }
}
