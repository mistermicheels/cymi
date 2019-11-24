package com.mistermicheels.cymi.common.error;

import java.util.Optional;

@SuppressWarnings("serial")
public class InvalidRequestException extends RuntimeException {

    private InvalidRequestExceptionType type;

    public InvalidRequestException(String message, InvalidRequestExceptionType type,
            Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public InvalidRequestException(String message, InvalidRequestExceptionType type) {
        super(message);
        this.type = type;
    }

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRequestException(String message) {
        super(message);
    }

    public Optional<InvalidRequestExceptionType> getType() {
        return Optional.ofNullable(this.type);
    }

}
