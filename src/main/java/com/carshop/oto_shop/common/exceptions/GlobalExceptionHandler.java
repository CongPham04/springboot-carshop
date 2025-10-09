package com.carshop.oto_shop.common.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException e) {
            ErrorCode errorCode = e.getErrorCode();
            ErrorResponse errorResponse = new ErrorResponse(
                    errorCode.getCode(),
                    errorCode.getMessage(),
                    errorCode.getHttpStatus().value()
            );
            return ResponseEntity
                    .status(errorCode.getHttpStatus().value())
                    .body(errorResponse);
    }

    @ExceptionHandler(value = DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getErrorCode().getCode(),
                e.getDmessage(),
                e.getErrorCode().getHttpStatus().value()
        );
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getErrorCode().getCode(),
                e.getBrmessage(),
                e.getErrorCode().getHttpStatus().value()
        );
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ErrorCode.METHOD_NOT_ALLOWED.getMessage());
        errorResponse.setStatus(ErrorCode.METHOD_NOT_ALLOWED.getHttpStatus().value());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrorCode(ErrorCode.METHOD_NOT_ALLOWED.getCode());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getHttpStatus().value());
        errorResponse.setMessage(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setErrorCode(ErrorCode.UNSUPPORTED_MEDIA_TYPE.getCode());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCode.FILE_UPLOAD_ERROR.getCode());
        errorResponse.setMessage(ErrorCode.FILE_UPLOAD_ERROR.getMessage());
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(ErrorCode.FILE_SIZE_EXCEEDED.getHttpStatus().value());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = LockedException.class)
    public ResponseEntity<ErrorResponse> handleLockedException(LockedException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCode.ACCOUNT_BANNED.getCode());
        errorResponse.setMessage(ErrorCode.ACCOUNT_BANNED.getMessage());
        errorResponse.setStatus(ErrorCode.ACCOUNT_BANNED.getHttpStatus().value());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(ErrorCode.ACCOUNT_INACTIVE.getCode());
        errorResponse.setMessage(ErrorCode.ACCOUNT_INACTIVE.getMessage());
        errorResponse.setStatus(ErrorCode.ACCOUNT_INACTIVE.getHttpStatus().value());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
     //   errorResponse.setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage() + " - Exception:" + e.getClass().getSimpleName());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        errorResponse.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }
}
