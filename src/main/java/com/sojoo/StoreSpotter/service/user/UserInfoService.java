package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.entity.user.User;
import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.service.mail.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class UserInfoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserService userService;
    private final MailService mailService;

    public UserInfoService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserService userService, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }


    // --------------- 비밀번호 재발급 -------------
    @Transactional
    public String updateUserPw(String email) throws Exception {
        Optional<User> user = userService.findUser(email);
        if (user.isPresent()) {
            String code = mailService.sendPwMail(email);

            user.get().updatePassword(passwordEncoder.encode(code));
//            userRepository.save(user.get());
            return "Successfully reissuePassword";
        } else {
            throw new NoSuchElementException("존재하지 않는 이메일입니다");
        }
    }



    // --------------- 이메일 찾기 -------------
    public List<String> findUserEmail(String username, String userPhone) throws IllegalStateException {
        List<User> users = userRepository.findByNicknameAndPhone(username, userPhone);
        List<String> userEmail = new ArrayList<>();
        for (User user : users) {
            String email = user.getUsername();
            userEmail.add(email);
        }
        if (users.size() <= 0) {
//            throw new IllegalStateException("User not found");
            userEmail = null;
            log.info("User not found");
        }

        return userEmail;
    }

    // --------------- Nickname 변경 -------------
    @Transactional
    public void modifyNickname(User user, String nickname){
        user.updateNickname(nickname);
    }

    // --------------- Phone 변경 -------------
    @Transactional
    public void modifyPhone(User user, String phone){
        user.updatePhone(phone);
    }

    // --------------- 비밀번호 변경 -------------
    @Transactional
    public ResponseEntity<String> modifyPassword(User user, String currentPassword, String password){
        if(checkCurrentPassword(user, currentPassword)){
            user.updatePassword(passwordEncoder.encode(password));
            return new ResponseEntity<>("success", HttpStatus.OK);
        } else{
            System.out.println(user.getPassword() + "with"+ passwordEncoder.encode(currentPassword));
            return new ResponseEntity<>("incorrect password", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkCurrentPassword(User user, String currentPassword){
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }



}
