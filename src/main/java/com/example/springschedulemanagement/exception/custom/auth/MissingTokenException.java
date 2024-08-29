package com.example.springschedulemanagement.exception.custom.auth;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.UNAUTHORIZED)
public class MissingTokenException extends BaseException {
    public MissingTokenException(String message) {
        super(message);
    }
}