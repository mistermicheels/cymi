package com.mistermicheels.cymi.web.api.output;

public class ApiSuccessResponse {

    private final boolean success = true;
    private final Object id;

    public ApiSuccessResponse() {
        this(null);
    }

    public ApiSuccessResponse(Object id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getId() {
        return id;
    }

}
