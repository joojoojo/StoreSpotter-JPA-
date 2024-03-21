package com.sojoo.StoreSpotter.controller.user;


import com.sojoo.StoreSpotter.dto.user.LoginDto;
import com.sojoo.StoreSpotter.dto.user.UserDto;
import com.sojoo.StoreSpotter.jwt.dto.TokenDto;
import com.sojoo.StoreSpotter.jwt.jwt.TokenProvider;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import com.sojoo.StoreSpotter.service.user.UserService;
import com.sojoo.StoreSpotter.service.user.UserValidateService;
import com.sojoo.StoreSpotter.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.concurrent.TimeUnit;

import static com.sojoo.StoreSpotter.util.CookieUtil.*;

@Slf4j
@RestController
@RequestMapping("/user/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;
    private final UserService userService;
    private final UserValidateService userValidateService;
    private final static int COOKIE_EXPIRE_SECONDS = 3600;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, CookieUtil cookieUtil, RedisService redisService, UserService userService, UserValidateService userValidateService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.userValidateService = userValidateService;
        this.redisService = redisService;
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            Cookie cookie = getCookie(request, "access_token");
            if (cookie != null){
                String username = tokenProvider.getUsernameFromToken(cookie.getValue());
                redisService.delValues(username);
                System.out.println("redisDel : " + username);
            }
            return ResponseEntity.ok("logout");
        } catch (Exception e) {
            log.error("로그아웃 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그아웃 중 오류 발생");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> loginProcess(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.createAccessToken(authentication);
            String username = tokenProvider.getUsernameFromToken(accessToken);
            String refreshToken = tokenProvider.createRefreshToken(authentication);

            addCookie(response, "access_token", accessToken, COOKIE_EXPIRE_SECONDS);
            // redis 저장
            redisService.setValues(username, refreshToken, 30 , TimeUnit.DAYS);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {

        // 이메일 중복 검사
        String username = userDto.getUsername();
        ResponseEntity<String> checkDuplicateEmail = userValidateService.checkDuplicateEmail(username);
        if (userValidateService.checkDuplicateEmail(userDto.getUsername()).getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return checkDuplicateEmail;
        }

        // 이메일 코드 검사
        String checkMailCodeResult = userValidateService.checkMailCode(userDto);

        if ("notEqualMailCode".equals(checkMailCodeResult)) {
            return new ResponseEntity<>("notEqualMailCode", HttpStatus.BAD_REQUEST);
        } else if ("expirationMailCode".equals(checkMailCodeResult)) {
            return new ResponseEntity<>("expirationMailCode", HttpStatus.BAD_REQUEST);
        }

        userService.signup(userDto);
        return new ResponseEntity<>("Successfully sign-up", HttpStatus.OK);    }

}