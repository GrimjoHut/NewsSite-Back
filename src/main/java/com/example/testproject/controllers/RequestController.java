package com.example.testproject.controllers;

import com.example.testproject.models.entities.Request;
import com.example.testproject.models.models.RequestDTO;
import com.example.testproject.repositories.RequestRepository;
import com.example.testproject.services.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping("/newRequest")
    public ResponseEntity createRequest(@RequestBody RequestDTO requestDTO){
        return requestService.createRequest(requestDTO);
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
    public ResponseEntity acceptRequest(Integer id){
        return requestService.acceptRequest(id);
    }

    @DeleteMapping("/reject_request/{id}")
    public ResponseEntity rejectRequest(Integer id){
        return requestService.deleteRequest(id);
    }
}
