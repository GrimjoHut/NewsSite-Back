package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.GeneralException;
import com.example.testproject.exceptions.custom.PostNotFoundException;
import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.enums.StatusEnum;
import com.example.testproject.models.models.Dto.PostDto;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.models.requests.PostRequest;
import com.example.testproject.repositories.PostRepository;
import com.example.testproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final CommunityService communityService;

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }


    // Метод для получения постов по конкретному паблику
    public Page<Post> getPublishedPostsByCommunity(Long communityId, Pageable pageable) {
        Page<Post> posts = postRepository.findByCommunityIdAndStatusOrderByCreatedDateDesc(communityId, StatusEnum.PUBLISHED, pageable);
        return posts;
    }

    public Page<Post> getPostsForSubscribedCommunities(CustomUserDetails userDetails, Pageable pageable) {
        Set<Community> subscribedCommunities = userDetails.getUser().getSubscribes();
        List<Long> communityIds = subscribedCommunities.stream()
                .map(Community::getId)
                .collect(Collectors.toList());

        Page<Post> posts = postRepository.findByCommunityIdInAndStatusOrderByCreatedDateDesc(communityIds, StatusEnum.PUBLISHED, pageable);
        return posts;
    }

    public Page<Post> getRequestedPostsByCommunity(Long communityId, Pageable pageable){
        Page<Post> posts = postRepository.findByCommunityIdAndStatusOrderByCreatedDateDesc(communityId, StatusEnum.REQUESTED, pageable);
        return posts;
    }

    public void createCommunityPost(PostRequest postRequest, List<MultipartFile> files, CustomUserDetails userDetails) {
        Post post = new Post();
        post.setUser(userDetails.getUser());
        post.setHeader(postRequest.getHeader());
        post.setDescription(postRequest.getDescription());
        post.setCommunity(communityService.findById(postRequest.getCommunityId()));
        post.setCreatedDate(OffsetDateTime.now());
        postRepository.save(post);
        for (MultipartFile file : files) {
            try {
                Image image = imageService.uploadImage(file, "image-bucket");
                image.setPost(post);
                post.getImages().add(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (userDetails
                .getCommunityAuthorities(post.getCommunity())
                .stream()
                .anyMatch(a -> a.equals(CommunityRoleEnum.ROLE_ADMIN))){
            post.setStatus(StatusEnum.PUBLISHED);
        } else {
            post.setStatus(StatusEnum.REQUESTED);
        }
        postRepository.save(post);
    }

    public Post acceptCommunityPost(Long id){
        Post post = this.findById(id);
        if (post.getStatus().equals(StatusEnum.REQUESTED)) {
            post.setStatus(StatusEnum.PUBLISHED);
            post.setCreatedDate(OffsetDateTime.now());
            postRepository.save(post);
            return post;
        } else throw new GeneralException(400, "Post allready published");
    }

    public void deletePost(Long id){
        Post post = this.findById(id);
        postRepository.delete(post);
    }
}
