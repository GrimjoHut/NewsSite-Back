package com.example.testproject.services;


import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.GeneralException;
import com.example.testproject.exceptions.custom.FriendsNotFoundException;
import com.example.testproject.models.entities.Friends;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.FriendStatusEnum;
import com.example.testproject.repositories.FriendsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepository friendsRepository;
    private final UserService userService;

    public Page<Friends> findByUserIdAndStatus(Long userId, Pageable pageable, FriendStatusEnum status){
        return friendsRepository.findByUserIdAndStatus(userId, pageable, status);
    }

    public Friends findByUserAndFriend(User user, User friend){
        return friendsRepository.findByUserAndFriend(user, friend)
                .orElseThrow(FriendsNotFoundException::new);
    }
    public Page<User> findFriends(Long userId, Pageable pageable){;
        return this
                .findByUserIdAndStatus(userId, pageable, FriendStatusEnum.FRIEND)
                .map(Friends::getFriend);
    }


    public Friends addFriend(CustomUserDetails userDetails, Long userId){
        User user = userDetails.getUser();
        User friend = userService.findById(userId);
        if (friendsRepository.findByUserAndFriend(user, friend).isPresent())
            throw new GeneralException(400, "Friend connection already exists");
        Friends friendshipUser = new Friends();
        Friends friendshipFriend = new Friends();

        friendshipUser.setUser(user);
        friendshipUser.setFriend(friend);
        friendshipUser.setStatus(FriendStatusEnum.REQUESTED);

        friendshipFriend.setUser(friend);
        friendshipFriend.setFriend(user);
        friendshipUser.setStatus(FriendStatusEnum.WAITING_FOR_ANSWER);

        friendsRepository.save(friendshipUser);
        friendsRepository.save(friendshipFriend);


        user.getFriends().add(friendshipUser);
        friend.getFriends().add(friendshipFriend);
        userService.saveUser(user);
        userService.saveUser(friend);

        return friendshipUser;
    }


    public Friends acceptFriend(CustomUserDetails userDetails, Long userId){
        User user = userDetails.getUser();
        User friend = userService.findById(userId);

        Friends friendshipUser = this.findByUserAndFriend(user, friend);
        Friends friendshipFriend = this.findByUserAndFriend(friend, user);

        if (!friendshipFriend.getStatus().equals(FriendStatusEnum.FRIEND)
            && !friendshipUser.getStatus().equals(FriendStatusEnum.FRIEND)) {

            friendshipUser.setStatus(FriendStatusEnum.FRIEND);
            friendshipFriend.setStatus(FriendStatusEnum.FRIEND);
            friendsRepository.save(friendshipFriend);
            friendsRepository.save(friendshipUser);
            return friendshipUser;
        } else throw new GeneralException(400, "Wrong data");
    }

    public void deleteFriendRequest(CustomUserDetails userDetails, Long userId){
        User user = userDetails.getUser();
        User friend = userService.findById(userId);

        Friends friendshipUser = this.findByUserAndFriend(user, friend);
        Friends friendshipFriend = this.findByUserAndFriend(friend, user);

        if (friendshipFriend.getStatus().equals(FriendStatusEnum.WAITING_FOR_ANSWER)
                && friendshipUser.getStatus().equals(FriendStatusEnum.REQUESTED)){
                friendsRepository.delete(friendshipUser);
                friendsRepository.delete(friendshipFriend);
        } else throw new GeneralException(400, "Wrong data");
    }

    public Friends deleteFriend(CustomUserDetails userDetails, Long userId){
        User user = userDetails.getUser();
        User friend = userService.findById(userId);

        Friends friendshipUser = this.findByUserAndFriend(user, friend);
        Friends friendshipFriend = this.findByUserAndFriend(friend, user);

        if (friendshipFriend.getStatus().equals(FriendStatusEnum.FRIEND)
                && friendshipUser.getStatus().equals(FriendStatusEnum.FRIEND)) {

            friendshipUser.setStatus(FriendStatusEnum.WAITING_FOR_ANSWER);
            friendshipFriend.setStatus(FriendStatusEnum.REQUESTED);
            friendsRepository.save(friendshipFriend);
            friendsRepository.save(friendshipUser);
            return friendshipUser;
        } else throw new GeneralException(400, "Wrong data");
    }
}
