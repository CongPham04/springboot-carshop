package com.carshop.oto_shop.common.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ACCOUNT_NOT_FOUND("ACCOUNT_NOT_FOUND", "Tài khoản không tồn tại!", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("USER_NOT_FOUND","Người dùng không tồn tại!", HttpStatus.NOT_FOUND),
    CARCATEGORY_NOT_FOUND("CARCATEGORY_NOT_FOUND", "Danh mục không còn tồn tại!", HttpStatus.NOT_FOUND),
    CAR_NOT_FOUND("CARCATE_NOT_FOUND", "Sản phẩm không còn tồn tại!", HttpStatus.NOT_FOUND),
    FILE_NOT_FOUND("FILE_NOT_FOUND","File không tồn tại!", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "Lỗi máy chủ!", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_ERROR("FILE_UPLOAD_ERROR", "Lỗi khi upload file!", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SIZE_EXCEEDED("FILE_SIZE_EXCEEDED", "Kích thước vượt quá giới hạn cho phép!", HttpStatus.PAYLOAD_TOO_LARGE),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "Định dạng dữ liệu không được hỗ trợ!", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    DUPLICATE_KEY("DUPLICATE_KEY","Trùng lặp dữ liệu!",HttpStatus.CONFLICT),
    UNKNOWN("UNKNOWN_ERROR", "Lỗi không xác định!", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("BAD_REQUEST", "Dữ liệu đầu vào không hợp lệ!", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "Phương thức HTTP không được hỗ trợ!", HttpStatus.METHOD_NOT_ALLOWED),
    INVALID_OR_EXPIRED_REFRESH_TOKEN("INVALID_OR_EXPIRED_REFRESH_TOKEN", "Refresh token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),;
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
