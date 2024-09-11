package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.GeneralException;
import com.example.testproject.exceptions.custom.PermissionNotAllowedException;
import com.example.testproject.exceptions.custom.PostNotFoundException;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.enums.CommentPermissionEnum;
import com.example.testproject.models.enums.PostPermissionEnum;
import com.example.testproject.models.entities.UserBoard;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.enums.StatusEnum;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.models.requests.PostRequest;
import com.example.testproject.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final CommunityService communityService;
    private final UserBoardService userBoardService;
    private final UserSecurityService userSecurityService;

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public Page<Post> getPosts(Specification<Post> spec, Pageable pageable){
        return postRepository.findAll(spec, pageable);
    }

    public Boolean commentPermission(CustomUserDetails userDetails, Long postId){
        Post post = this.findById(postId);
        switch(post.getCommentaryPermission()) {
            case SUBS_ONLY:
                return (userSecurityService.isSubscriber(userDetails, post.getCommunity().getId()));
            case FRIENDS_ONLY:
                return (userSecurityService.isFriend(userDetails, post.getUserBoard().getUser()));
            case CLOSED:
                return false;
            default:
                return true;
        }
    }

    public Boolean manipulatePostPermission(CustomUserDetails userDetails, Long postId){
        Post post = this.findById(postId);
        if (post.getCommunity() != null) return userSecurityService.isCommunityAdmin(userDetails, postId);
        if (post.getUserBoard() != null) return (userDetails.getUser().equals(post.getUserBoard().getUser()));
        return false;
    }


    public Post createPost(PostRequest postRequest, List<MultipartFile> files, CustomUserDetails userDetails) {
        if ((postRequest.getCommunityId() != null && postRequest.getUserBoardId() != null)
                || (postRequest.getUserBoardId() == null && postRequest.getCommunityId() == null))
            throw new GeneralException(400, "Only userBoardId or communityId");
        Post post = postRequest.mapToEntity(postRequest);
        post.setUser(userDetails.getUser());
        for (MultipartFile file : files) {
            try {
                Image image = imageService.uploadImage(file, "image-bucket");
                image.setPost(post);
                post.getImages().add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (postRequest.getCommunityId() != null) {
            post.setCommunity(communityService.findById(postRequest.getCommunityId()));
            if (userDetails
                    .getCommunityAuthorities(post.getCommunity())
                    .stream()
                    .anyMatch(a -> a.equals(CommunityRoleEnum.ROLE_ADMIN))) {
                post.setStatus(StatusEnum.PUBLISHED);
            } else {
                post.setStatus(StatusEnum.REQUESTED);
            }
        }
        if (postRequest.getUserBoardId() != null) {
            UserBoard userBoard = userBoardService.findById(postRequest.getUserBoardId());
            if (userBoard.getPostPermission().equals(PostPermissionEnum.EVERYBODY)) {
                post.setUserBoard(userBoardService.findById(postRequest.getUserBoardId()));
                post.setStatus(StatusEnum.REQUESTED);
            }
            if (userBoard.getPostPermission().equals(PostPermissionEnum.FRIENDS_ONLY)){
                if (userSecurityService.isFriend(userDetails, userBoard.getUser())) {
                    post.setUserBoard(userBoard);
                } else throw new PermissionNotAllowedException();
            }
            if (userBoard.getPostPermission().equals(PostPermissionEnum.USER_ONLY)){
                if (userBoard.getUser().equals(userDetails.getUser())) {
                    post.setUserBoard(userBoard);
                } else throw new PermissionNotAllowedException();
            }
        }
            postRepository.save(post);
        return post;
    }

    public Post acceptCommunityPost(Long postId){
        Post post = this.findById(postId);
        if (post.getStatus().equals(StatusEnum.REQUESTED)) {
            post.setStatus(StatusEnum.PUBLISHED);
            post.setCreatedDate(OffsetDateTime.now());
            postRepository.save(post);
            return post;
        } else throw new GeneralException(400, "You cannot accept not requested post");
    }

    public Post declineCommunityPost(Long postId){
        Post post = this.findById(postId);
        if (post.getStatus().equals(StatusEnum.REQUESTED)) {
            post.setStatus(StatusEnum.DECLINED);
            postRepository.save(post);
            return post;
        } else throw new GeneralException(400, "You cannot decline not requested post");
    }

    public Post deletePost(CustomUserDetails userDetails, Long postId){
        Post post = this.findById(postId);
        if (manipulatePostPermission(userDetails, postId)) {
            if (post.getStatus() != StatusEnum.DELETED) {
                post.setStatus(StatusEnum.DELETED);
                postRepository.save(post);
                return post;
            } else throw new GeneralException(400, "Post already deleted");
        } else throw new PermissionNotAllowedException();
    }

    public Post changeCommPermission(CustomUserDetails userDetails, Long postId, CommentPermissionEnum permission){
        if (manipulatePostPermission(userDetails, postId)){
            Post post = this.findById(postId);
            post.setCommentaryPermission(permission);
            postRepository.save(post);
            return post;
        } else throw new PermissionNotAllowedException();
    }
}
