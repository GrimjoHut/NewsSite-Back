package com.example.testproject.specifications;

import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.UserBoard;
import com.example.testproject.models.enums.StatusEnum;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class PostSpecifications {

    public static Specification<Post> hasStatus(StatusEnum status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction(); // Без фильтра по статусу
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }


    public static Specification<Post> hasReports(Boolean reported) {
        return (root, query, criteriaBuilder) -> {
            if (reported == null) {
                return criteriaBuilder.conjunction();
            }
            if (reported) {
                return criteriaBuilder.isNotEmpty(root.get("reports"));
            } else {
                return criteriaBuilder.isEmpty(root.get("reports"));
            }
        };
    }

    public static Specification<Post> inCommunities(List<Long> communityIds) {
        return (root, query, criteriaBuilder) -> {
            if (communityIds == null || communityIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("community").get("id").in(communityIds);
        };
    }

    public static Specification<Post> inUserBoards(List<Long> userBoardIds){
        return (root, query, criteriaBuilder) -> {
            if (userBoardIds == null || userBoardIds.isEmpty()){
                return criteriaBuilder.conjunction();
            }
            return root.get("userBoard").get("id").in(userBoardIds);
        };
    }

    public static Specification<Post> inCommunity(Long communityId) {
        return (root, query, criteriaBuilder) -> {
            if (communityId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("community").get("id"), communityId);
        };
    }

    public static Specification<Post> inUserBoard(Long userBoardId){
        return (root, query, criteriaBuilder) -> {
            if (userBoardId == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("userBoard").get("id"), userBoardId);
        };
    }
}

