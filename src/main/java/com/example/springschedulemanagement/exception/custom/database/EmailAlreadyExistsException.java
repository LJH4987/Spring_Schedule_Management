package com.example.springschedulemanagement.exception.custom.database;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
