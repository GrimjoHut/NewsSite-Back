    package com.example.testproject.repositories;
    
    import com.example.testproject.models.entities.File;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    
    @Repository
    public interface FileRepository extends JpaRepository<File, Long> {
    }
