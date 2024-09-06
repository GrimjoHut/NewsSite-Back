package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.custom.UserNotFoundException;
import com.example.testproject.exceptions.custom.WrongAuthorizationParametrsRequest;
import com.example.testproject.models.entities.File;
import com.example.testproject.models.entities.Image;
import com.example.testproject.models.models.Dto.LoginDto;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.repositories.UserRepository;
import com.example.testproject.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
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
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final ImageService imageService;
    private final EmailSenderService mailSender;

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findByNickname(String nickname){
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    public void createUser(LoginDto loginDTO) {
        if (userRepository.findByNickname(loginDTO.getNickname()).isPresent())
            throw new WrongAuthorizationParametrsRequest();
        User user = new User();
        user.setNickname(loginDTO.getNickname());
        user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
        user.setEmail(loginDTO.getEmail());
        String token = verificationTokenService.createVerificationToken(user);
        mailSender.sendVerifyCode(user, token);
        userRepository.save(user);
    }

    public void verifyUser(String token){
        verificationTokenService.validateVerificationToken(token);
    }

    public JWTResponse login(LoginDto loginDTO) {
        User user = this.findByNickname(loginDTO.getNickname());
        if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String token = JWTUtil.generateToken(user.getNickname());
            return new JWTResponse(token);
        } else {
            throw new WrongAuthorizationParametrsRequest();
        }
    }

    public void changeAvatar(MultipartFile file, CustomUserDetails userDetails){
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
    }

    public ResponseEntity<String> giveRole(String actingUserNickname, String targetUserNickname, RoleEnum role) {
        Optional<User> actingUserOpt = userRepository.findByNickname(actingUserNickname);
        Optional<User> targetUserOpt = userRepository.findByNickname(targetUserNickname);

        if (actingUserOpt.isPresent() && targetUserOpt.isPresent()) {
            User actingUser = actingUserOpt.get();
            User targetUser = targetUserOpt.get();

            if (!actingUser.getRoles().stream().anyMatch(r -> r.canAssign(role))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to assign this role");
            }

            if (targetUser.getRoles().contains(role)) {
                return ResponseEntity.status(HttpStatus.OK).body("User already has this role");
            } else {
                targetUser.getRoles().add(role);
                if (role == RoleEnum.ROLE_ADMIN && !targetUser.getRoles().contains(RoleEnum.ROLE_MODER)) {
                    targetUser.getRoles().add(RoleEnum.ROLE_MODER);
                }
                userRepository.save(targetUser);
                return ResponseEntity.status(HttpStatus.OK).body("Role was given");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    public ResponseEntity<String> takeRole(String actingUserNickname, String targetUserNickname, RoleEnum role) {
        Optional<User> actingUserOpt = userRepository.findByNickname(actingUserNickname);
        Optional<User> targetUserOpt = userRepository.findByNickname(targetUserNickname);

        if (actingUserOpt.isPresent() && targetUserOpt.isPresent()) {
            User actingUser = actingUserOpt.get();
            User targetUser = targetUserOpt.get();

            if (!actingUser.getRoles().stream().anyMatch(r -> r.canRevoke(role))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to revoke this role");
            }

            if (!targetUser.getRoles().contains(role)) {
                return ResponseEntity.status(HttpStatus.OK).body("User doesn't have this role");
            } else {
                targetUser.getRoles().remove(role);
                if (role == RoleEnum.ROLE_MODER && targetUser.getRoles().contains(RoleEnum.ROLE_ADMIN)) {
                    targetUser.getRoles().remove(RoleEnum.ROLE_ADMIN);
                }
                userRepository.save(targetUser);
                return ResponseEntity.status(HttpStatus.OK).body("Role was taken");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
