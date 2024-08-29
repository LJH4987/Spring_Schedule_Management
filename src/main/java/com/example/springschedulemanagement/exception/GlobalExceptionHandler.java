package com.example.springschedulemanagement.exception;

import com.example.springschedulemanagement.exception.custom.auth.*;
import com.example.springschedulemanagement.exception.custom.database.DatabaseException;
import com.example.springschedulemanagement.exception.custom.database.EmailAlreadyExistsException;
import com.example.springschedulemanagement.exception.custom.other.WeatherApiException;
import com.example.springschedulemanagement.exception.custom.resource.CommentNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.RoleNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.ScheduleNotFoundException;
import com.example.springschedulemanagement.exception.custom.resource.UserNotFoundException;
import com.example.springschedulemanagement.exception.custom.schedule.UserScheduleConflictException;
import com.example.springschedulemanagement.exception.custom.validation.InvalidPageSizeException;
import com.example.springschedulemanagement.exception.custom.validation.MethodArgumentTypeMismatchException;
import com.example.springschedulemanagement.exception.enums.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ErrorResponse createErrorResponse(HttpStatus status, String error, String errorCode, String userMessage, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .errorCode(errorCode)
                .userMessage(userMessage)
                .path(request.getDescription(false))
                .build();
    }

    private ResponseEntity<ErrorResponse> handleException(HttpStatus status, String error, String errorCode, String userMessage, WebRequest request, Exception ex) {
        logger.error("Exception: {}, ErrorCode: {}, Message: {}", error, errorCode, ex.getMessage(), ex);
        ErrorResponse errorResponse = createErrorResponse(status, error, errorCode, userMessage, request);
        return new ResponseEntity<>(errorResponse, status);
    }

    // 잘못된 요청 데이터에 대한 처리
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            InvalidPageSizeException.class
    })
    public ResponseEntity<ErrorResponse> handleValidationExceptions(Exception ex, WebRequest request) {
        String errorCode = "VALIDATION001";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessage = "유효하지 않은 요청 데이터입니다.";

        if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            errorMessage = bindingResult.hasFieldErrors() ? bindingResult.getFieldError().getDefaultMessage() : errorMessage;
        } else if (ex instanceof InvalidPageSizeException) {
            errorCode = "SCHEDULE004";
            errorMessage = "요청 페이지 사이즈가 유효하지 않습니다.";
        }

        return handleException(status, "잘못된 요청 데이터입니다.", errorCode, errorMessage, request, ex);
    }

    // 인증 및 권한 예외 처리
    @ExceptionHandler({
            TokenExpiredException.class,
            AuthenticationException.class,
            MalformedJwtException.class,
            InvalidTokenException.class,
            MissingTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthExceptions(Exception ex, WebRequest request) {
        String errorCode = "AUTH001";
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String errorMessage = "인증 오류가 발생했습니다.";

        if (ex instanceof TokenExpiredException) {
            errorCode = "AUTH002";
            errorMessage = "토큰이 만료되었습니다. 다시 로그인해 주세요.";
        } else if (ex instanceof MalformedJwtException) {
            errorCode = "AUTH004";
            errorMessage = "손상된 JWT 토큰입니다.";
        } else if (ex instanceof InvalidTokenException) {
            errorCode = "AUTH005";
            errorMessage = "유효하지 않은 토큰입니다. 다시 로그인해 주세요.";
        } else if (ex instanceof MissingTokenException) {
            errorCode = "AUTH006";
            status = HttpStatus.BAD_REQUEST;
            errorMessage = "요청에 토큰이 포함되어 있지 않습니다. 로그인 후 다시 시도해 주세요.";
        }

        return handleException(status, "인증 오류가 발생했습니다.", errorCode, errorMessage, request, ex);
    }

    // 데이터베이스 무결성 및 중복 예외 처리
    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            DataIntegrityViolationException.class,
            InvalidDataAccessApiUsageException.class,
            UserScheduleConflictException.class
    })
    public ResponseEntity<ErrorResponse> handleDatabaseExceptions(Exception ex, WebRequest request) {
        String errorCode = "DB001";
        HttpStatus status = HttpStatus.CONFLICT;
        String userMessage = "데이터베이스 오류가 발생했습니다.";

        if (ex instanceof InvalidDataAccessApiUsageException) {
            errorCode = "DB003";
            userMessage = "잘못된 데이터베이스 사용이 발생했습니다.";
        } else if (ex instanceof DataIntegrityViolationException) {
            errorCode = "DB004";
            userMessage = "데이터 무결성 오류가 발생했습니다.";
        } else if (ex instanceof EmailAlreadyExistsException) {
            errorCode = "USER001";
            userMessage = "이미 사용 중인 이메일 주소입니다.";
        } else if (ex instanceof UserScheduleConflictException) {
            errorCode = "SCHEDULE002";
            userMessage = "이미 해당 일정에 등록된 사용자입니다.";
        }

        return handleException(status, "데이터베이스 오류가 발생했습니다.", errorCode, userMessage, request, ex);
    }

    // 리소스를 찾을 수 없음 예외 처리
    @ExceptionHandler({
            ScheduleNotFoundException.class,
            CommentNotFoundException.class,
            RoleNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleResourceNotFoundExceptions(Exception ex, WebRequest request) {
        String errorCode = "RESOURCE_NOT_FOUND";
        HttpStatus status = HttpStatus.NOT_FOUND;
        String userMessage = "리소스를 찾을 수 없습니다.";

        if (ex instanceof ScheduleNotFoundException) {
            errorCode = "SCHEDULE001";
            userMessage = "해당 일정이 존재하지 않습니다.";
        } else if (ex instanceof CommentNotFoundException) {
            errorCode = "COMMENT001";
            userMessage = "해당 댓글이 존재하지 않습니다.";
        } else if (ex instanceof RoleNotFoundException) {
            errorCode = "ROLE001";
            userMessage = "해당 역할이 존재하지 않습니다.";
        } else if (ex instanceof UserNotFoundException) {
            errorCode = "USER002";
            userMessage = "해당 사용자가 존재하지 않습니다.";
        }

        return handleException(status, "리소스를 찾을 수 없습니다.", errorCode, userMessage, request, ex);
    }

    // 서버 관련 예외 처리
    @ExceptionHandler({
            AccessDeniedException.class,
            WeatherApiException.class,
            DatabaseException.class,
            DuplicateRoleAssignmentException.class
    })
    public ResponseEntity<ErrorResponse> handleServerExceptions(Exception ex, WebRequest request) {
        String errorCode = "SERVER_ERROR";
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String userMessage = "서버 오류가 발생했습니다.";

        if (ex instanceof AccessDeniedException) {
            errorCode = "AUTH003";
            status = HttpStatus.FORBIDDEN;
            userMessage = "이 작업을 수행할 권한이 없습니다.";
        } else if (ex instanceof WeatherApiException) {
            errorCode = "WEATHER001";
            userMessage = "날씨 정보를 가져오는 중 오류가 발생했습니다.";
        } else if (ex instanceof DatabaseException) {
            errorCode = "DB002";
            userMessage = "데이터베이스 작업 중 오류가 발생했습니다.";
        } else if (ex instanceof DuplicateRoleAssignmentException) {
            errorCode = "AUTH007";
            userMessage = "이미 해당 사용자에게는 해당 권한이 부여되어 있습니다. ";
        }

        return handleException(status, "서버 오류가 발생했습니다.", errorCode, userMessage, request, ex);
    }

    // 중앙화된 예외 처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {
        HttpStatus status = mapErrorCodeToHttpStatus(ex.getErrorCode());
        return handleException(status, ex.getMessage(), ex.getErrorCode().name(), ex.getMessage(), request, ex);
    }

    // 예상외 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다.", "GENERIC001", "서버 오류가 발생했습니다. 잠시 후 다시 시도 하거나 혹은 관리자에게 문의해 주세요.", request, ex);
    }

    // ErrorCode에 따른 HttpStatus 매핑
    private HttpStatus mapErrorCodeToHttpStatus(ErrorCode errorCode) {
        switch (errorCode) {
            case VALIDATION_ERROR:
                return HttpStatus.BAD_REQUEST;
            case DATABASE_ERROR:
                return HttpStatus.INTERNAL_SERVER_ERROR;
            case NETWORK_ERROR:
                return HttpStatus.SERVICE_UNAVAILABLE;
            case AUTH_ERROR:
                return HttpStatus.UNAUTHORIZED;
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
