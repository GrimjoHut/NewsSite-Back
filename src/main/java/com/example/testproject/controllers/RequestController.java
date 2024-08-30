package com.example.testproject.controllers;

import com.example.testproject.models.DTO.RequestDTO;
import com.example.testproject.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping(value = "/newRequest", consumes = {"multipart/form-data"})
    public ResponseEntity createRequest(
            @RequestPart("request") RequestDTO requestDTO,
            @RequestPart("files") List<MultipartFile> files) throws Exception {
        return requestService.createRequest(requestDTO, files);
    }

    @GetMapping("/test")
    public ResponseEntity Test(){
        return ResponseEntity.ok("TEST");
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable Integer id){
        return requestService.getRequest(id);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestDTO>> getRequests(Integer offset){
        return requestService.getRequests(offset);
    }

    @PostMapping("/accept_request/{id}")
    public ResponseEntity acceptRequest(@PathVariable Integer id){
        return requestService.acceptRequest(id);
    }

    @DeleteMapping("/reject_request/{id}")
    public ResponseEntity rejectRequest(Integer id){
        return requestService.deleteRequest(id);
    }
}
