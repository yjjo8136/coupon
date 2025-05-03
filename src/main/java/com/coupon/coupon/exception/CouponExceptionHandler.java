package com.coupon.coupon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CouponExceptionHandler {
    @ExceptionHandler(CouponException.class)
    public ResponseEntity<CouponResponse> handleCustomException(CouponException e) {
        CouponResponse errorResponse = new CouponResponse(e.getStatus(), e.getMessage(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(e.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CouponResponse> handleGeneralException(Exception e) {
        CouponResponse errorResponse = new CouponResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "정의되지 않은 오류가 발생했습니다.", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
