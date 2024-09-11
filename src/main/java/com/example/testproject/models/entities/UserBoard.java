package com.example.testproject.models.entities;

import com.example.testproject.models.enums.PostPermissionEnum;
import com.example.testproject.models.enums.ReadPermissionEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "userBoard")
public class UserBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "userBoard", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private User user;

    @Column(name = "readPermission")
    private ReadPermissionEnum readPermission = ReadPermissionEnum.EVERYBODY;

    @Column
    @OneToMany(mappedBy = "userBoard", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Column(name = "postPermission")
    private PostPermissionEnum postPermission = PostPermissionEnum.EVERYBODY;

    public UserBoard(User user){
        this.user = user;
    }
}
