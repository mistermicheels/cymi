package com.mistermicheels.cymi.web.api.error;

import org.springframework.http.HttpStatus;

public class ApiError {
    
    private final HttpStatus status;
    private final String message;
    private final String type;
    
    public ApiError(HttpStatus status, String message, String type) {
        this.status = status;
        this.message = message;
        this.type = type;
    }
    
    public ApiError(HttpStatus status, String message) {
        this(status, message, null);
    }

    public int getStatus() {
        return status.value();
    }

    public String getTitle() {
        return status.getReasonPhrase();
    }

    public String getMessage() {
        return message;
    }
    
    public String getType() {
        return this.type;
    }
}
