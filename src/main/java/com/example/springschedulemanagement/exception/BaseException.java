package com.example.springschedulemanagement.exception;

import com.example.springschedulemanagement.exception.enums.ErrorCode;
import lombok.Getter;

import java.io.Serial;

@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final ErrorCode DEFAULT_ERROR_CODE = ErrorCode.DEFAULT_ERROR;

    private final ErrorCode errorCode;

    public BaseException(String message) {
        this(message, null, null);
    }

    public BaseException(String message, Throwable cause) {
        this(message, cause, null);
    }

    public BaseException(String message, ErrorCode errorCode) {
        this(message, null, errorCode);
    }

    public BaseException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode != null ? errorCode : DEFAULT_ERROR_CODE;
        logException(message, cause);
    }

    private void logException(String message, Throwable cause) {
        ExceptionLogger.logException(message, cause, errorCode.getCode());
    }
}