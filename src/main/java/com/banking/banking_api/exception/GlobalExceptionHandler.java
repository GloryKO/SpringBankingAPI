package com.banking.banking_api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice watches ALL controllers
// When any exception is thrown anywhere in the app,
// Spring checks here first and runs the matching @ExceptionHandler method
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            HttpStatus status, String message) {

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }
        /* should probably use this for all responses instead of Map<String, Object> --- IGNORE ---
        private ResponseEntity<ErrorResponse> buildErrorResponse(
                HttpStatus status, String message) {

            ErrorResponse body = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now().toString())
                    .status(status.value())
                    .error(status.getReasonPhrase())
                    .message(message)
                    .build();

            return ResponseEntity.status(status).body(body);
        }

        */  
    @ExceptionHandler(AppExceptions.DuplicateResourceException.class)// handles DuplicateResourceException anywhere in the app
    public ResponseEntity<Map<String, Object>> handleDuplicate(
            AppExceptions.DuplicateResourceException ex) {
        log.warn("Duplicate resource: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AppExceptions.ResourceNotFoundException.class) // handles ResourceNotFoundException anywhere in the app
    public ResponseEntity<Map<String, Object>> handleNotFound(
            AppExceptions.ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AppExceptions.UnauthorizedException.class) // handles UnauthorizedException anywhere in the app
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            AppExceptions.UnauthorizedException ex) {
        log.warn("Unauthorized: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AppExceptions.InsufficientBalanceException.class) // handles InsufficientBalanceException anywhere in the app
    public ResponseEntity<Map<String, Object>> handleInsufficientBalance(
            AppExceptions.InsufficientBalanceException ex) {
        log.warn("Insufficient balance: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AppExceptions.InvalidOtpException.class) // handles InvalidOtpException anywhere in the app
    public ResponseEntity<Map<String, Object>> handleInvalidOtp(
            AppExceptions.InvalidOtpException ex) {
        log.warn("Invalid OTP: {}", ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Handles @Valid failures — returns each field's error message
    @ExceptionHandler(MethodArgumentNotValidException.class) // handles validation errors when @Valid fails in any controller
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Catch-all — any unhandled exception returns 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred");
    }
}