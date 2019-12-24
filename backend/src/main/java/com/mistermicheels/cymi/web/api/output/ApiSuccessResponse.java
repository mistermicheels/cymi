package com.mistermicheels.cymi.web.api.output;

public class ApiSuccessResponse {

    private final boolean success = true;
    private final Long id;

    public ApiSuccessResponse() {
        this(null);
    }

    public ApiSuccessResponse(Long id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public Long getId() {
        return this.id;
    }

}
