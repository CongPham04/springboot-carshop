package com.carshop.oto_shop.common.exceptions;

public class BadRequestException extends AppException {
    private final String brmessage;
    public BadRequestException(String message) {
        super(ErrorCode.BAD_REQUEST);
        this.brmessage = message;
    }

    public String getBrmessage() {
        return brmessage;
    }
}
