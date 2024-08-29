package com.example.springschedulemanagement.exception.custom.auth;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends BaseException {
    public InvalidTokenException(String message) {
        super(message);
    }
}