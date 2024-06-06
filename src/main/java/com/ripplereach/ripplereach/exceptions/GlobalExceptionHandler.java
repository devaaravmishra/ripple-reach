package com.ripplereach.ripplereach.exceptions;

import com.ripplereach.ripplereach.dtos.ErrorResponseDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        System.out.println("ERROR WHILE CREATING ERRORS: " + ex.getBindingResult().getAllErrors());
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = null;
            String errorMessage = error.getDefaultMessage();

            if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
            } else if (error instanceof ObjectError) {
                fieldName = error.getObjectName();
            }

            errors.put(fieldName, errorMessage);
        });

        ErrorResponseDto responseDto = ErrorResponseDto
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .type(String.valueOf(HttpStatus.BAD_REQUEST))
                .message("Validation failed")
                .errors(errors)
                .build();

        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestBodyNotReadableException(HttpMessageNotReadableException ex) {
       ErrorResponseDto errorResponse = ErrorResponseDto
               .builder()
               .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
               .type(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY))
               .title("Unprocessable entity")
               .message(ex.getMessage())
               .build();

       return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ErrorResponseDto errorResponse = ErrorResponseDto
                .builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .type(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY))
                .title("Unprocessable entity")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponseDto errorResponse = ErrorResponseDto
                .builder()
                .status(HttpStatus.NOT_FOUND.value())
                .type(String.valueOf(HttpStatus.NOT_FOUND))
                .title("Entity not found!")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityAlreadyExistsException(EntityExistsException ex) {
        ErrorResponseDto errorResponse = ErrorResponseDto
                .builder()
                .status(HttpStatus.CONFLICT.value())
                .type(String.valueOf(HttpStatus.CONFLICT))
                .title("Entity already exists!")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(RippleReachException.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        ErrorResponseDto errorResponse = ErrorResponseDto
                .builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .type(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                .title("An unexpected error occurred")
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
