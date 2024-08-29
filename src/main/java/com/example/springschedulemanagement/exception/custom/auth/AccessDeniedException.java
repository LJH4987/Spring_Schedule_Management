package com.example.springschedulemanagement.exception.custom.auth;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends BaseException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
