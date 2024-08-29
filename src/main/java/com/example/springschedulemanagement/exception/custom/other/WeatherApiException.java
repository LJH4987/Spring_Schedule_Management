package com.example.springschedulemanagement.exception.custom.other;

import com.example.springschedulemanagement.exception.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WeatherApiException extends BaseException {
    public WeatherApiException(String message, Exception e) {
        super(message, e);
    }
}
