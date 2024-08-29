package com.example.springschedulemanagement.exception.custom.database;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DatabaseException extends BaseException {
    public DatabaseException(String message) {
        super(message);
    }
}