package com.mistermicheels.cymi.web.api.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mistermicheels.cymi.config.web.WebProperties;

@Service
public class ApiErrorFactoryService {

    private final boolean includeStackTraceInApiError;

    @Autowired
    public ApiErrorFactoryService(WebProperties webProperties) {
        this.includeStackTraceInApiError = webProperties.shouldIncludeStackTraceInApiError();
    }

    public ApiError createApiError(HttpStatus status, String message, Throwable cause,
            String type) {
        if (this.includeStackTraceInApiError) {
            return new ApiError(status, message, type, cause);
        } else {
            return new ApiError(status, message, type);
        }
    }

    public ApiError createApiError(HttpStatus status, String message, Throwable cause) {
        if (this.includeStackTraceInApiError) {
            return new ApiError(status, message, cause);
        } else {
            return new ApiError(status, message);
        }
    }

}
