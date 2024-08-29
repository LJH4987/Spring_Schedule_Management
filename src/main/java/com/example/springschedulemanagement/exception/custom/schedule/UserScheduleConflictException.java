package com.example.springschedulemanagement.exception.custom.schedule;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserScheduleConflictException extends BaseException {
    public UserScheduleConflictException(String message) {
        super(message);
    }
}
