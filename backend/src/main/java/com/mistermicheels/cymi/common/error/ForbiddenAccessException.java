package com.mistermicheels.cymi.common.error;

@SuppressWarnings("serial")
public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenAccessException(String message) {
        super(message);
    }

}
