package com.example.testproject.models.models.requests;

import com.example.testproject.models.entities.Post;
import com.example.testproject.models.enums.CommentPermissionEnum;
import lombok.Data;

@Data
public class PostRequest {
    private String description;
    private Long communityId;
    private Long userBoardId;
    private String header;
    private CommentPermissionEnum permission;

    public Post mapToEntity(PostRequest postRequest){
        Post post = new Post();
        post.setHeader(postRequest.getHeader());
        post.setDescription(postRequest.getDescription());
        if (permission != null)
            post.setCommentaryPermission(permission);
        return post;
    }
}
