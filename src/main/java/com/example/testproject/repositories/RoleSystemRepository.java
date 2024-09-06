package com.example.testproject.repositories;

import com.example.testproject.models.entities.RoleSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleSystemRepository extends JpaRepository<RoleSystem, Long> {
}
