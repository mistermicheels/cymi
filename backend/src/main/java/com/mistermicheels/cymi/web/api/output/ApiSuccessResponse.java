package com.mistermicheels.cymi.web.api.output;

import org.springframework.lang.Nullable;

public class ApiSuccessResponse {

    private final boolean success = true;

    @Nullable
    private final Long id;

    public ApiSuccessResponse() {
        this(null);
    }

    public ApiSuccessResponse(@Nullable Long id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public @Nullable Long getId() {
        return this.id;
    }

}
