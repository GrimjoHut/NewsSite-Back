package com.example.testproject.repositories;

import com.example.testproject.models.entities.Commentary;
import com.example.testproject.models.entities.Likes;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.LikesEnum;
import com.example.testproject.models.models.responses.LikeJPQLRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("SELECT new com.example.testproject.models.models.responses.LikeJPQLRes(" +
            "SUM(CASE WHEN l.reaction = 'LIKE' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN l.reaction = 'DISLIKE' THEN 1 ELSE 0 END)) " +
            "FROM Likes l " +
            "WHERE (:postId IS NULL OR l.post.id = :postId) " +
            "AND (:commentId IS NULL OR l.comment.id = :commentId)")
    LikeJPQLRes countLikesAndDislikes(@Param("postId") Long postId, @Param("commentId") Long commentId);

    Optional<Likes> findByPostAndUser(Post post, User user);

    Optional<Likes> findByCommentAndUser(Commentary comment, User user);
}
