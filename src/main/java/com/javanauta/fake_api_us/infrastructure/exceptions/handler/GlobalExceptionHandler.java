package com.javanauta.fake_api_us.infrastructure.exceptions.handler;

import com.javanauta.fake_api_us.infrastructure.exceptions.BusinessException;
import com.javanauta.fake_api_us.infrastructure.exceptions.ConflictException;
import com.javanauta.fake_api_us.infrastructure.exceptions.UnprocessableEntityException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        return response(ex.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ErrorResponse> handleUnprocessableEntityException(UnprocessableEntityException ex, HttpServletRequest request) {
        return response(ex.getMessage(), request, HttpStatus.UNPROCESSABLE_ENTITY, LocalDateTime.now());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return response(ex.getMessage(), request, HttpStatus.CONFLICT, LocalDateTime.now());
    }

    private ResponseEntity<ErrorResponse> response(final String message, final HttpServletRequest request, final HttpStatus status, LocalDateTime data) {
        return ResponseEntity.status(status).body(new ErrorResponse(message, data, status.value(), request.getRequestURI()));
    }
}