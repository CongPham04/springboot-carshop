package com.carshop.oto_shop.common.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Tài khoản không tồn tại!", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("USER_NOT_FOUND","Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Lỗi máy chủ!", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_KEY("DUPLICATE_KEY","Trùng lặp dữ liệu!",HttpStatus.CONFLICT),
    UNKNOWN("UNKNOWN_ERROR", "Lỗi không xác định!", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("BAD_REQUEST", "Dữ liệu đầu vào không hợp lệ!", HttpStatus.BAD_REQUEST);
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
