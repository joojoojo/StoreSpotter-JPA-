package com.sojoo.StoreSpotter.jwt.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 401
    INVALID_TOKEN("101_INVALID_TOKEN", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("102_EXPIRED_TOKEN", "만료된 토큰입니다."),
    ACCESS_DENIED("103_ACCESS_DENIED", "접근 권한이 없는 사용자 요청입니다.");



    private final String code;
    private final String message;


}