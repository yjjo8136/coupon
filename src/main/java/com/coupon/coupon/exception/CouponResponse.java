package com.coupon.coupon.exception;

import org.springframework.http.HttpStatus;

public class CouponResponse<T> {
    private final int status;
    private final String message;
    private final T body;

    // 성공 응답 (body가 없는 경우)
    public CouponResponse(String message) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.body = null;
    }

    // 성공 응답 (body가 존재하는 경우)
    public CouponResponse(String message, T body) {
        this.status = HttpStatus.OK.value();
        this.message = message;
        this.body = body;
    }

    // 실패 응답
    public CouponResponse(int status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }

}
