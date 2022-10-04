package com.mistermicheels.cymi.web.api.error;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class ApiError {

    private final HttpStatus status;
    private final String message;

    @Nullable
    private final String type;

    @Nullable
    private final String stackTrace;

    public ApiError(HttpStatus status, String message, @Nullable String type,
            @Nullable Throwable stackTraceSource) {
        this.status = status;
        this.message = message;
        this.type = type;

        if (stackTraceSource != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            stackTraceSource.printStackTrace(printWriter);
            this.stackTrace = stringWriter.toString();
        } else {
            this.stackTrace = null;
        }
    }

    public ApiError(HttpStatus status, String message, Throwable cause) {
        this(status, message, null, cause);
    }

    public ApiError(HttpStatus status, String message, String type) {
        this(status, message, type, null);
    }

    public ApiError(HttpStatus status, String message) {
        this(status, message, null, null);
    }

    public int getStatus() {
        return this.status.value();
    }

    public String getTitle() {
        return this.status.getReasonPhrase();
    }

    public String getMessage() {
        return this.message;
    }

    public @Nullable String getType() {
        return this.type;
    }

    public @Nullable String getStackTrace() {
        return this.stackTrace;
    }
}
