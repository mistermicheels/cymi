package com.mistermicheels.cymi.web.api.error;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mistermicheels.cymi.common.error.ForbiddenAccessException;
import com.mistermicheels.cymi.common.error.InvalidRequestException;

@ControllerAdvice
public class ApiResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final static String INVALID_INPUT_STRUCTURE_MESSAGE = "Invalid input structure";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ApiErrorFactoryService apiErrorFactoryService;

    @Autowired
    public ApiResponseEntityExceptionHandler(ApiErrorFactoryService apiErrorFactoryService) {
        this.apiErrorFactoryService = apiErrorFactoryService;
    }

    @ExceptionHandler(value = { AuthenticationException.class })
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException exception, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = exception.getMessage();
        ApiError apiError = this.apiErrorFactoryService.createApiError(status, message, exception);

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    @ExceptionHandler(value = { InvalidRequestException.class })
    protected ResponseEntity<Object> handleInvalidRequestException(
            InvalidRequestException exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = exception.getMessage();
        ApiError apiError;

        if (exception.getType().isPresent()) {
            String type = exception.getType().get().name();
            apiError = this.apiErrorFactoryService.createApiError(status, message, exception, type);
        } else {
            apiError = this.apiErrorFactoryService.createApiError(status, message, exception);
        }

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    @ExceptionHandler(value = { ForbiddenAccessException.class })
    protected ResponseEntity<Object> handleAccessNotAllowedException(
            ForbiddenAccessException exception, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        String message = exception.getMessage();
        ApiError apiError = this.apiErrorFactoryService.createApiError(status, message, exception);

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    @ExceptionHandler(value = { Exception.class })
    protected ResponseEntity<Object> handleUnkownException(Exception exception,
            WebRequest request) {
        this.logger.error("Internal server error returned", exception);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Unknown error, please try again";

        ApiError apiError = this.apiErrorFactoryService.createApiError(status, message, exception);

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        String message = INVALID_INPUT_STRUCTURE_MESSAGE;

        FieldError firstError = exception.getBindingResult().getFieldError();

        if (firstError != null) {
            message = message + ": property " + firstError.getField() + " "
                    + firstError.getDefaultMessage();
        }

        ApiError apiError = this.apiErrorFactoryService.createApiError(status, message, exception);

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        Exception rootCause = (Exception) exception.getMostSpecificCause();
        String message;

        if (rootCause instanceof UnrecognizedPropertyException) {
            UnrecognizedPropertyException unrecognizedPropertyException = (UnrecognizedPropertyException) rootCause;
            String propertyPath = this.getJsonPropertyPath(unrecognizedPropertyException);
            message = INVALID_INPUT_STRUCTURE_MESSAGE + ": unknown property " + propertyPath;
        } else if (rootCause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) rootCause;
            String propertyPath = this.getJsonPropertyPath(invalidFormatException);

            message = INVALID_INPUT_STRUCTURE_MESSAGE + ": invalid format for property "
                    + propertyPath;

        } else {
            message = INVALID_INPUT_STRUCTURE_MESSAGE;
        }

        ApiError apiError = this.apiErrorFactoryService.createApiError(status, message, exception);

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    private String getJsonPropertyPath(JsonMappingException exception) {
        return exception.getPath().stream().map(reference -> reference.getFieldName())
                .collect(Collectors.joining("/"));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    private ResponseEntity<Object> createBasicApiErrorResponse(Exception exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = exception.getMessage();
        ApiError apiError = this.apiErrorFactoryService.createApiError(status, message, exception);

        return this.handleExceptionInternal(exception, apiError, new HttpHeaders(), status,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException exception, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            HttpMessageNotWritableException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException exception,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException exception, HttpHeaders headers, HttpStatus status,
            WebRequest request) {
        return this.createBasicApiErrorResponse(exception, headers, status, request);
    }

}
