package com.example.testproject.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class File {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    private String bucket;
    private String name;

    @Column(unique = true)
    private String path;

    private Long size;
    private String fileExtension;

    @Basic
    private OffsetDateTime releaseTime = OffsetDateTime.now();
}
