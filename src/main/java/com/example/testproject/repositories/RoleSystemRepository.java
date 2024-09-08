package com.example.testproject.repositories;

import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.RoleSystem;
import com.example.testproject.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleSystemRepository extends JpaRepository<RoleSystem, Long> {

    RoleSystem findByUserAndCommunity(User user, Community community);

}
