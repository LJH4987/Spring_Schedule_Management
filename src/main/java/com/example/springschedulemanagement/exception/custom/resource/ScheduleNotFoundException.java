package com.example.springschedulemanagement.exception.custom.resource;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends BaseException {
    public ScheduleNotFoundException(String message) {
        super(message);
    }
}
