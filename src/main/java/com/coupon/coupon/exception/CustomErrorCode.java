package com.coupon.coupon.exception;

public enum CustomErrorCode {
    LOGIN_REQUIRED(401, "로그인 후 사용 가능합니다."),
    COUPON_OUT_OF_STOCK(400, "쿠폰 재고가 부족합니다."),
    COUPON_ISSUANCE_ALREADY_USED(400, "이미 사용된 쿠폰입니다."),
    COUPON_EXPIRED(400, "쿠폰이 만료되었습니다."),
    COUPON_ALREADY_ISSUED(400, "이미 발급받은 쿠폰입니다."),
    COUPON_NOT_FOUND(404, "쿠폰을 찾을 수 없습니다."),
    COUPON_ISSUANCE_NOT_FOUND(404, "쿠폰 발급을 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    ACCESS_DENIED(403, "관리자 권한을 가진 사용자만 해당 작업을 수행할 수 있습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다.");

    private final int status;
    private final String message;

    CustomErrorCode(int status, String message) {
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
