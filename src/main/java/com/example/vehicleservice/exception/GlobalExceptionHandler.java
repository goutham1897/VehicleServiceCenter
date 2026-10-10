package com.example.vehicleservice.exception;

import com.example.vehicleservice.constants.ExceptionConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    // =========================================================
    // RESOURCE NOT FOUND
    // =========================================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            ResourceNotFoundException exception,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());

        response.setStatus(
                HttpStatus.NOT_FOUND.value()
        );

        response.setError("NOT_FOUND");

        response.setMessage(
                exception.getMessage()
        );

        response.setPath(
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }


    // =========================================================
    // DUPLICATE RESOURCE
    // =========================================================

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse>
    handleDuplicateResourceException(
            DuplicateResourceException exception,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());

        response.setStatus(
                HttpStatus.CONFLICT.value()
        );

        response.setError("DUPLICATE_RESOURCE");

        response.setMessage(
                exception.getMessage()
        );

        response.setPath(
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }


    // =========================================================
    // BUSINESS EXCEPTION
    // =========================================================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse>
    handleBusinessException(
            BusinessException exception,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());

        response.setStatus(
                HttpStatus.BAD_REQUEST.value()
        );

        response.setError("BUSINESS_ERROR");

        response.setMessage(
                exception.getMessage()
        );

        response.setPath(
                request.getRequestURI()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }


    // =========================================================
    // VALIDATION EXCEPTION
    // =========================================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse>
    handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        Map<String, String> errors =
                new HashMap<>();

        for (FieldError fieldError :
                exception.getBindingResult()
                        .getFieldErrors()) {

            errors.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        ValidationErrorResponse response =
                new ValidationErrorResponse();

        response.setTimestamp(LocalDateTime.now());

        response.setStatus(
                HttpStatus.BAD_REQUEST.value()
        );

        response.setError(
                ExceptionConstants.VALIDATION_FAILED
        );

        response.setMessage(
                "Request validation failed"
        );

        response.setPath(
                request.getRequestURI()
        );

        response.setErrors(errors);

        return ResponseEntity
                .badRequest()
                .body(response);
    }


    // =========================================================
    // GENERAL EXCEPTION
    // =========================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleGeneralException(
            Exception exception,
            HttpServletRequest request) {

        ErrorResponse response =
                new ErrorResponse();

        response.setTimestamp(LocalDateTime.now());

        response.setStatus(
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        response.setError(
                "INTERNAL_SERVER_ERROR"
        );

        response.setMessage(
                ExceptionConstants.INTERNAL_SERVER_ERROR
        );

        response.setPath(
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }


    // =========================================================
    // ERROR RESPONSE CLASS
    // =========================================================

    public static class ErrorResponse {

        private LocalDateTime timestamp;

        private int status;

        private String error;

        private String message;

        private String path;


        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(
                LocalDateTime timestamp) {

            this.timestamp = timestamp;
        }


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }


        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }


    // =========================================================
    // VALIDATION ERROR RESPONSE
    // =========================================================

    public static class ValidationErrorResponse
            extends ErrorResponse {

        private Map<String, String> errors;


        public Map<String, String> getErrors() {
            return errors;
        }

        public void setErrors(
                Map<String, String> errors) {

            this.errors = errors;
        }
    }
}