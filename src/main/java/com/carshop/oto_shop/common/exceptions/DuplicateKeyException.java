package com.carshop.oto_shop.common.exceptions;

public class DuplicateKeyException extends AppException{
    private final String dmessage;
    public DuplicateKeyException(String message) {
        super(ErrorCode.DUPLICATE_KEY);
        this.dmessage = message;
    }

    public String getDmessage() {
        return dmessage;
    }
}
