package com.example.springschedulemanagement.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionLogger {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionLogger.class);

    public static void logException(String message, Throwable cause, int errorCode) {
        if (cause != null) {
            logger.error("예외 발생: {}, 원인: {}, 에러 코드: {}", message, cause.getMessage(), errorCode, cause);
        } else {
            logger.error("예외 발생: {}, 에러 코드: {}", message, errorCode);
        }
    }
}