package com.carshop.oto_shop.common.exceptions;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(ErrorCode.INTERNAL_SERVER_ERROR.getMessage() + " - Exception:" + e.getClass().getSimpleName());
        errorResponse.setErrorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        errorResponse.setStatus(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus().value());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }
}
