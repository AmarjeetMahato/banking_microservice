package com.payment_service.globalException;

import com.payment_service.resposne.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> {
                            String msg = error.getDefaultMessage();
                            return (msg != null) ? msg : "Invalid value";
                        },
                        (existing, replacement) -> existing
                ));

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .message("Validation failed")
                .status(HttpStatus.BAD_REQUEST)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> HandleValidationExceptions(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrors());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> ResourceNotFoundException(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.NOT_FOUND).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> BadCredentialsException(BadCredentialsException ex) {
        String message = ex.getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.UNAUTHORIZED).build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> BadRequestException(BadRequestException ex) {
        String message = ex.getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.BAD_REQUEST).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> ResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        String message = ex.getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.CONFLICT).build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ApiErrorResponse> internalServerErrorExceptions(InternalServerError ex) {
        String message = ex.getMessage();
        ApiErrorResponse response = ApiErrorResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
