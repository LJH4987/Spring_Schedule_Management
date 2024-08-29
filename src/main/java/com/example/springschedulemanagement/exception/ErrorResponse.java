package com.example.springschedulemanagement.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    @JsonIgnore
    private String error;

    private String errorCode;

    private String path;

    private String userMessage;

}