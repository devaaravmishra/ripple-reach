package com.ripplereach.ripplereach.exceptions;

import com.ripplereach.ripplereach.constants.Messages;
import com.ripplereach.ripplereach.dtos.ErrorResponseDto;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = null;
              String errorMessage = error.getDefaultMessage();

              if (error instanceof FieldError) {
                fieldName = ((FieldError) error).getField();
              } else if (error instanceof ObjectError) {
                fieldName = error.getObjectName();
              }

              errors.put(fieldName, errorMessage);
            });

    ErrorResponseDto responseDto =
        ErrorResponseDto.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .type(String.valueOf(HttpStatus.BAD_REQUEST))
            .message(Messages.VALIDATION_FAILED)
            .errors(errors)
            .build();

    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponseDto> handleRequestBodyNotReadableException(
      HttpMessageNotReadableException ex) {
    ErrorResponseDto errorResponse =
        ErrorResponseDto.builder()
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .type(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY))
            .title(Messages.UNPROCESSABLE_ENTITY)
            .message(ex.getMessage().split(":")[0])
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(InvalidContentTypeException.class)
  public ResponseEntity<ErrorResponseDto> handleInvalidContentTypeException(InvalidContentTypeException ex) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto
            .builder()
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .type(HttpStatus.UNPROCESSABLE_ENTITY.toString())
            .title(Messages.UNPROCESSABLE_ENTITY)
            .message(ex.getMessage())
            .build();

    return new ResponseEntity<>(errorResponseDto, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto
            .builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .type(HttpStatus.BAD_REQUEST.toString())
            .title(ex.getErrorCode().toUpperCase())
            .message(String.format("Parameter '%s' should be of type '%s'",
                    ex.getName(),
                    Objects.requireNonNull(ex.getRequiredType()).getSimpleName()))
            .build();

    return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(
      HttpMediaTypeNotSupportedException ex) {
    ErrorResponseDto errorResponse =
        ErrorResponseDto.builder()
            .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
            .type(String.valueOf(HttpStatus.UNPROCESSABLE_ENTITY))
            .title(Messages.UNPROCESSABLE_ENTITY)
            .message(ex.getMessage())
            .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(
      EntityNotFoundException ex) {
    ErrorResponseDto errorResponse =
        ErrorResponseDto.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .type(String.valueOf(HttpStatus.NOT_FOUND))
            .title(Messages.ENTITY_NOT_FOUND)
            .message(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ex) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto
            .builder()
            .status(HttpStatus.FORBIDDEN.value())
            .type(String.valueOf(HttpStatus.FORBIDDEN))
            .title(Messages.FORBIDDEN_ACCESS_DENIED)
            .message(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponseDto);
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorResponseDto> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
    ErrorResponseDto errorResponseDto = ErrorResponseDto
            .builder()
            .status(HttpStatus.METHOD_NOT_ALLOWED.value())
            .type(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED))
            .title(Messages.METHOD_NOT_ALLOWED)
            .message(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponseDto);
  }

  @ExceptionHandler(EntityExistsException.class)
  public ResponseEntity<ErrorResponseDto> handleEntityAlreadyExistsException(
      EntityExistsException ex) {
    ErrorResponseDto errorResponse =
        ErrorResponseDto.builder()
            .status(HttpStatus.CONFLICT.value())
            .type(String.valueOf(HttpStatus.CONFLICT))
            .title(Messages.ENTITY_ALREADY_EXISTS)
            .message(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
  }

  @ExceptionHandler(RippleReachException.class)
  public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
    ErrorResponseDto errorResponse =
        ErrorResponseDto.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .type(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
            .title(Messages.UNEXPECTED_ERROR)
            .message(ex.getMessage())
            .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
