package com.example.springschedulemanagement.exception.enums;

public enum ErrorCode {
    DEFAULT_ERROR(1000),
    VALIDATION_ERROR(1001),
    DATABASE_ERROR(1002),
    NETWORK_ERROR(1003),
    AUTH_ERROR(1004);

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}