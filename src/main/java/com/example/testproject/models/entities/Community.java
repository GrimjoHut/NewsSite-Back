package com.example.testproject.models.entities;

import com.example.testproject.models.enums.CommunityRoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.management.relation.Role;
import java.util.Set;

@Entity
@Getter
@Setter
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "descripton")
    private String description;

    @Column(name = "avatar")
    @OneToOne(mappedBy = "community", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Image avatar;

    @Column(name = "posts")
    private Set<Post> posts;

    @Column(name = "subscribers")
    private Set<User> subscribers;
}
