package com.example.testproject.services;

import com.example.testproject.models.entities.Request;
import com.example.testproject.models.DTO.RequestDTO;
import com.example.testproject.repositories.RequestRepository;
import com.example.testproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final ImageService imageService;

    public ResponseEntity createRequest(RequestDTO requestDTO, List<MultipartFile> files) throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now();
        Request request = new Request();
        request.setCreatedAt(localDateTime);
        request.setUser(userRepository.findById(requestDTO.getUser().getId()).get());
        request.setHeader(requestDTO.getHeader());
        request.setDescription(requestDTO.getDescription());
        for (MultipartFile file : files) {
            String imageUrl = imageService.uploadImage(file, "image-bucket");
            request.getImageUrls().add(imageUrl);
        }
        requestRepository.save(request);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity getRequests(Integer offset) {
        int pageSize = 10;

        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        List<RequestDTO> requestDTOList = requestRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(RequestDTO::new)
                .collect(Collectors
                        .toList());

        return ResponseEntity.status(HttpStatus.OK).body(requestDTOList);
    }

    public ResponseEntity getRequest(Integer id) {

        Optional<Request> optionalRequest = requestRepository.findById(id);

        if (optionalRequest.isPresent()) {
            RequestDTO requestDTO = new RequestDTO(optionalRequest.get());
            return ResponseEntity.status(HttpStatus.OK).body(requestDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
    }

    public ResponseEntity acceptRequest(Integer id){
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            RequestDTO requestDTO = new RequestDTO(optionalRequest.get());
            postService.createPost(requestDTO);
            this.deleteRequest(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Request not found");
        }
    }

    public ResponseEntity deleteRequest(Integer id){
        requestRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
