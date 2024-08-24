package com.example.testproject.repositories;

import com.example.testproject.models.entities.Request;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
