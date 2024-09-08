package com.example.testproject.repositories;

import com.example.testproject.models.entities.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<Community, Long> {

    Page<Community> findBySubscribersId(Long userId, Pageable pageable);

}
