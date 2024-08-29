package com.example.springschedulemanagement.exception.custom.auth;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends BaseException {
    public AuthenticationException(String message) {
        super(message);
    }
}
