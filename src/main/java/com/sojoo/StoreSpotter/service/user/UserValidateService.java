package com.sojoo.StoreSpotter.service.user;

import com.sojoo.StoreSpotter.repository.user.UserRepository;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserValidateService {

    private final UserRepository userRepository;

    public UserValidateService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final String pwRegExp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$";
    private final String phoneRegExp = "\\d{11}";

    // 이메일 중복검사
    public ResponseEntity<String> checkDuplicateEmail(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            return new ResponseEntity<>("duplicateEmail", HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    // 모든 항목 일치 검사
    public ResponseEntity<String> notNullMemberInfo(UserDto userDto) {
        if (userDto.getNickname() == null || userDto.getNickname() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (userDto.getUsername() == null || userDto.getUsername() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (userDto.getPassword() == null || userDto.getPassword() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        } else if (userDto.getCheckPassword() == null || userDto.getCheckPassword() == "") {
            return new ResponseEntity<>("memberInfoNull", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 비밀번호 일치 검사
    public ResponseEntity<String> checkEqualPassword(UserDto userDto) {
        if (!userDto.getPassword().equals(userDto.getCheckPassword())) {
            return new ResponseEntity<>("notEqualPassword", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    // 비밀번호 정규식 검사
    public ResponseEntity<String> passwordRegExp(UserDto userDto) {
        if (!userDto.getPassword().matches(pwRegExp)) {
            return new ResponseEntity<>("passwordRegExp", HttpStatus.BAD_REQUEST);
        }
        return null;
    }


    // 전화번호 형식 검사
    public ResponseEntity<String> phoneRegExp(UserDto userDto) {
        if (!userDto.getPhone().matches(phoneRegExp)) {
            return new ResponseEntity<>("phoneRegExp", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}
