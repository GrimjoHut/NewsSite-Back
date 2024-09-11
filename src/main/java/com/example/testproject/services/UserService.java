package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.GeneralException;
import com.example.testproject.exceptions.custom.PermissionNotAllowedException;
import com.example.testproject.exceptions.custom.UserNotFoundException;
import com.example.testproject.exceptions.custom.WrongAuthorizationParametrsRequest;
import com.example.testproject.models.entities.Friends;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.enums.FriendStatusEnum;
import com.example.testproject.models.enums.StatusEnum;
import com.example.testproject.models.models.requests.LoginRequest;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.repositories.PostRepository;
import com.example.testproject.repositories.UserRepository;
import com.example.testproject.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.testproject.security.JWTResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenService verificationTokenService;
    private final ImageService imageService;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> findByNickname(String nickname){
        return userRepository.findByNickname(nickname);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public Page<User> findByCommunity(Long communityId, Pageable pageable){
        return userRepository.findBySubscribesId(communityId, pageable);
    }

    public User verifyUser(String token){
        User user = verificationTokenService.validateVerificationToken(token);
        user.setEnabled(true);
        user.getRoles().add(RoleEnum.ROLE_USER);
        userRepository.save(user);
        return user;
    }


    public User changeAvatar(MultipartFile file, CustomUserDetails userDetails){
        User user = userDetails.getUser();
        try {
            Image image = imageService.uploadImage(file, "image-bucket");
            image.setUser(user);
            if (user.getAvatar() != null) imageService.deleteImage(user.getAvatar());
            user.setAvatar(image);
        } catch (Exception e){
            e.printStackTrace();
        }
        userRepository.save(user);
        return user;
    }

    public User giveRole(CustomUserDetails userDetails,
                                           Long userId,
                                           RoleEnum role) {
        User targetUser = this.findById(userId);
        User actingUser = userDetails.getUser();

        if (!actingUser.getRoles().stream().anyMatch(r -> r.canAssign(role)))
            throw new PermissionNotAllowedException();

        if (targetUser.getRoles().contains(role)) {
            throw new GeneralException(400, "User already has this role");
        } else {
            targetUser.getRoles().add(role);
            if (role == RoleEnum.ROLE_ADMIN && !targetUser.getRoles().contains(RoleEnum.ROLE_MODER)) {
                targetUser.getRoles().add(RoleEnum.ROLE_MODER);
            }
            userRepository.save(targetUser);
            return targetUser;
        }
    }

    public User takeRole(CustomUserDetails userDetails,
                         Long userId,
                         RoleEnum role) {
        User targetUser = this.findById(userId);
        User actingUser = userDetails.getUser();

            if (!actingUser.getRoles().stream().anyMatch(r -> r.canRevoke(role))) {
                throw new PermissionNotAllowedException();
            }
            if (!targetUser.getRoles().contains(role)) {
                throw new GeneralException(400, "User doesnt have this role");
            } else {
                targetUser.getRoles().remove(role);
                if (role == RoleEnum.ROLE_MODER && targetUser.getRoles().contains(RoleEnum.ROLE_ADMIN)) {
                    targetUser.getRoles().remove(RoleEnum.ROLE_ADMIN);
                }
                userRepository.save(targetUser);
                return targetUser;
            }
    }
}
