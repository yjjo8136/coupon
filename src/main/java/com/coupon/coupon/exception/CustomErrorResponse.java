package com.coupon.coupon.exception;

public class CustomErrorResponse<T> {
    private final int status;
    private final String message;

    //private final T body;

    public CustomErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }



    public String getMessage() {
        return message;
    }
}
