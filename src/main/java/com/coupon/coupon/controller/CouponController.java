package com.coupon.coupon.controller;

import com.coupon.coupon.annotation.LoginUser;
import com.coupon.coupon.common.SessionConstant;
import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
import com.coupon.coupon.domain.User;
import com.coupon.coupon.exception.CouponResponse;
import com.coupon.coupon.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
public class CouponController {
    @Autowired
    private CouponService couponService;

    // 전체 쿠폰 목록 조회
    @GetMapping("/couponList")
    public CouponResponse<List<Coupon>> showCouponList() {
        List<Coupon> couponList = couponService.getAllCoupons();
        return new CouponResponse<List<Coupon>> ("쿠폰 목록 조회에 성공했습니다.", couponList);
    }

    // 쿠폰 추가
    @PostMapping("/addCoupon")
    public CouponResponse<Void> addCoupon(@RequestBody Coupon coupon) {
        couponService.createCoupon(coupon);
        return new CouponResponse<>("쿠폰 추가에 성공했습니다.");
    }

    // 쿠폰 발급
    @PostMapping("/issueCoupon")
    public CouponResponse<Void> issueCoupon(@RequestParam("couponId") Long couponId, @LoginUser User user) {
        Long userId = user.getId();
        couponService.issueCoupon(couponId, userId);
        return new CouponResponse<>("쿠폰이 성공적으로 발급되었습니다.");
    }

    // 사용자가 발급받은 모든 쿠폰 조회
    @GetMapping("/myCoupons")
    public CouponResponse<List<CouponIssuance>> showMyCoupons(@LoginUser User user) {
        Long userId = user.getId();
        List<CouponIssuance> myCouponList = couponService.getMyCoupons(userId);
        return new CouponResponse<>("내 쿠폰 목록 조회에 성공했습니다.", myCouponList);
    }

    // 쿠폰 사용
    @PostMapping("/useCoupon")
    public CouponResponse<Void> useCoupon(@RequestParam("couponIssuanceId") Long couponIssuanceId) {
        couponService.useCoupon(couponIssuanceId);
        return new CouponResponse<>("쿠폰 사용에 성공했습니다.");
    }
}
