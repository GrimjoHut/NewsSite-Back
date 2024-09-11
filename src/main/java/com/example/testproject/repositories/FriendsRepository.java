package com.example.testproject.repositories;

import com.example.testproject.models.entities.Friends;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.FriendStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface FriendsRepository extends JpaRepository<Friends, Long> {

    Optional<Friends> findByUserAndFriend(User user, User friend);

    Page<Friends> findByUserIdAndStatus(Long id, Pageable pageable, FriendStatusEnum status);

}
