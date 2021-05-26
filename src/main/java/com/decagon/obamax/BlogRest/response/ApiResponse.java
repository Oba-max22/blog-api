package com.decagon.obamax.BlogRest.response;

import lombok.Data;

@Data
public class ApiResponse {
    boolean successful;
    String message;

    public ApiResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }
}
