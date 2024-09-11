package com.example.testproject.specifications;

import com.example.testproject.models.entities.Commentary;
import org.springframework.data.jpa.domain.Specification;

public class CommentarySpecification {

    public static Specification<Commentary> toPost(Long postId){
        return (root, query, criteriaBuilder) -> {
            if (postId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("post").get("id"), postId);
        };
    }

    public static Specification<Commentary> toCommentary(Long commId){
        return (root, query, criteriaBuilder) -> {
            if (commId == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("parentCommentary").get("id"), commId);
        };
    }
}
