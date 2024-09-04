package com.example.testproject.services;

import com.example.testproject.models.entities.Image;
import com.example.testproject.models.models.Dto.PostDto;
import com.example.testproject.models.entities.Post;
import com.example.testproject.models.entities.User;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final ImageService imageService;

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }


    public Page<Post> getFivePosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAllByOrderByCreatedDateDesc(pageable);
       return posts;
    }

        public void createPost(PostDto postDto, List<MultipartFile> files) {
            Post post = new Post();
            post.setUser(userService.findByNickName(postDto.getAuthor().getNickname()));
            post.setHeader(postDto.getHeader());
            post.setDescription(postDto.getDescription());
            post.setCreatedDate(OffsetDateTime.now());

                        postRepository.save(post);

            // Загружаем изображения и связываем их с постом
            for (MultipartFile file : files) {
                try {
                    Image image = imageService.uploadImage(file, "image-bucket");

                    // Связываем изображение с постом
                    image.setPost(post);

                    // Сохраняем измененное изображение с привязкой к посту
                    post.getImages().add(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Сохраняем пост с привязанными изображениями
            postRepository.save(post);
        }
    public void deletePost(Long id){
        Post post = this.findById(id);
        postRepository.delete(post);
    }
}
