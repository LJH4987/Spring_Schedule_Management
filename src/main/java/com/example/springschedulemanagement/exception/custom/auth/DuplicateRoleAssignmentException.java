package com.example.springschedulemanagement.exception.custom.auth;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.CONFLICT)
public class DuplicateRoleAssignmentException extends BaseException {
    public DuplicateRoleAssignmentException(String message) {
        super(message);
    }
}
