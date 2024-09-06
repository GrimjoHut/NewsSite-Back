package com.example.testproject.models.entities;

import com.example.testproject.models.enums.CommunityRoleEnum;
import jakarta.persistence.*;

import javax.management.relation.Role;
import java.util.Set;

@Entity("role_system")
public class RoleSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column("community")
    private Community community;

    @Column(name = "user")
    private User user;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_community_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<CommunityRoleEnum> roles;
}
