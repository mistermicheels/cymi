package com.mistermicheels.cymi.config.web;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.web")
@Validated
public class WebProperties {

    private boolean includeStackTraceInApiError = false;

    public boolean shouldIncludeStackTraceInApiError() {
        return this.includeStackTraceInApiError;
    }

    void setIncludeStackTraceInApiError(boolean includeStackTraceInApiError) {
        this.includeStackTraceInApiError = includeStackTraceInApiError;
    }

}
