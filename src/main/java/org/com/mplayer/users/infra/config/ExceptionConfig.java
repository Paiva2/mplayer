package org.com.mplayer.users.infra.config;

import org.com.mplayer.users.domain.core.usecase.common.exception.core.BadRequestException;
import org.com.mplayer.users.domain.core.usecase.common.exception.core.ConflictException;
import org.com.mplayer.users.domain.core.usecase.common.exception.core.ForbiddenException;
import org.com.mplayer.users.domain.core.usecase.common.exception.core.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@RestControllerAdvice
public class ExceptionConfig {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleException(BadRequestException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.BAD_REQUEST.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleException(ConflictException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.CONFLICT.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleException(ForbiddenException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.FORBIDDEN.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleException(NotFoundException e) {
        Map<String, Object> errors = mapErrors(
            HttpStatus.NOT_FOUND.value(),
            e.getMessage(),
            e.getClass().getSimpleName()
        );

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> mapErrors(int status, String message, String exception) {
        return new LinkedHashMap<>() {{
            put("timestamp", new Date());
            put("status", status);
            put("message", message);
            put("exception", exception);
        }};
    }
}
