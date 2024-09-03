package com.sojoo.StoreSpotter.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // 400
    EMAIL_DUPLICATION(400,"USER-DUPLICATE-400","EMAIL DUPLICATED"),

    // 500
    USER_NOT_FOUND(500, "USER-NOT-FOUND", "USER NOT FOUND"),
    DATA_RECOMMEND_NOT_FOUND(500, "DATA-RECOMMEND-NOT-FOUND", "DATA(INDUSTRY) RECOMMEND NOT FOUND"),
    API_DATA_NOT_FOUND(500, "API-DATA-NOT-FOUND", "API DATA NOT FOUND"),
    DATA_PAIR_CREATE_FAILED(500, "DATA-PAIR-CREATE-FAILED", "DATA PAIR CREATE FAILED"),


    //421
    SMTP_SEND_FAILED(421, "SMTP-SEND-FAILED-421", "SMTP SEND FAILED");

    private final int status;
    private final String errorCode;
    private final String message;

}