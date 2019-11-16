package com.mistermicheels.cymi.web.api.error;

import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mistermicheels.cymi.common.error.InvalidRequestException;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String INVALID_INPUT_STRUCTURE_MESSAGE = "Invalid input structure";

    @ExceptionHandler(value = { AuthenticationException.class })
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception,
            WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(status, exception.getMessage());
        return handleExceptionInternal(exception, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError firstError = exception.getBindingResult().getFieldError();

        String message = INVALID_INPUT_STRUCTURE_MESSAGE + ": property " + firstError.getField() + " "
                + firstError.getDefaultMessage();

        ApiError apiError = new ApiError(status, message);
        return handleExceptionInternal(exception, apiError, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        Exception rootCause = (Exception) exception.getMostSpecificCause();
        String message;

        if (rootCause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException unrecognizedPropertyException = (UnrecognizedPropertyException) rootCause;
            String propertyPath = this.getJsonPropertyPath(unrecognizedPropertyException);
            message = INVALID_INPUT_STRUCTURE_MESSAGE + ": unknown property " + propertyPath;
        } else if (rootCause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;
            String propertyPath = this.getJsonPropertyPath(invalidFormatException);
            message = INVALID_INPUT_STRUCTURE_MESSAGE + ": invalid format for property " + propertyPath;

        } else {
            message = INVALID_INPUT_STRUCTURE_MESSAGE;
        }

        ApiError apiError = new ApiError(status, message);
        return handleExceptionInternal(exception, apiError, new HttpHeaders(), status, request);
    }

    private String getJsonPropertyPath(JsonMappingException exception) {
        return exception.getPath().stream().map(reference -> reference.getFieldName()).collect(Collectors.joining("/"));
    }

    @ExceptionHandler(value = { InvalidRequestException.class })
    protected ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException exception,
            WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError apiError;

        if (exception.getType().isPresent()) {
            apiError = new ApiError(status, exception.getMessage(), exception.getType().get().name());
        } else {
            apiError = new ApiError(status, exception.getMessage());
        }

        return handleExceptionInternal(exception, apiError, new HttpHeaders(), status, request);
    }
    
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(status, exception.getMessage());
        return handleExceptionInternal(exception, apiError, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleAnyOtherException(Exception exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiError = new ApiError(status, "Unknown error, please try again");
        return handleExceptionInternal(exception, apiError, new HttpHeaders(), status, request);
    }
}