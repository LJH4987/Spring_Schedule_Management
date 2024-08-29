package com.example.springschedulemanagement.service.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceLoggingUtil {

    private static final Map<Class<?>, Logger> loggerCache = new ConcurrentHashMap<>();

    private static Logger getLogger(Class<?> clazz) {
        return loggerCache.computeIfAbsent(clazz, LoggerFactory::getLogger);
    }

    public static void logInfo(Class<?> clazz, String message, Object... args) {
        Logger logger = getLogger(clazz);
        logger.info(message, args);
    }

    public static void logWarn(Class<?> clazz, String message, Object... args) {
        Logger logger = getLogger(clazz);
        logger.warn(message, args);
    }

    public static void logError(Class<?> clazz, String message, Object... args) {
        Logger logger = getLogger(clazz);
        logger.error(message, args);
    }

    public static void logDebug(Class<?> clazz, String message, Object... args) {
        Logger logger = getLogger(clazz);
        logger.debug(message, args);
    }
}
