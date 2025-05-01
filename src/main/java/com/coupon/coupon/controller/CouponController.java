package com.coupon.coupon.controller;

import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
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
    public List<Coupon> showCouponList(Model model) {
        return couponService.getAllCoupons();
    }

    // 쿠폰 추가
    @PostMapping("/addCoupon")
    public ResponseEntity<String> addCoupon(@RequestBody Coupon coupon) {
        couponService.createCoupon(coupon);
        return ResponseEntity.status(HttpStatus.CREATED).body("쿠폰이 성공적으로 추가되었습니다.");
    }

    // 쿠폰 발급
    @PostMapping("/issueCoupon")
    public ResponseEntity<String> issueCoupon(@RequestParam("couponId") Long couponId, HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션에서 현재 로그인한 사용자 정보 가져오기
        Object currentObj = session.getAttribute("currentUser");
        // 로그인한 사용자의 ID를 가져옴
        Long userId = ((com.coupon.coupon.domain.User) currentObj).getId();

        couponService.issueCoupon(couponId, userId);
        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 성공적으로 발급되었습니다.");
    }

    // 사용자가 발급받은 모든 쿠폰 조회
    @GetMapping("/myCoupons")
    public List<CouponIssuance> showMyCoupons(HttpSession session, Model model) {
        // 세션에서 현재 로그인한 사용자 정보 가져오기
        Object currentObj = session.getAttribute("currentUser");
        // 로그인한 사용자의 ID를 가져옴
        Long userId = ((com.coupon.coupon.domain.User) currentObj).getId();

        return couponService.getMyCoupons(userId);
    }

    // 쿠폰 사용
    @PostMapping("/useCoupon")
    public ResponseEntity<String> useCoupon(@RequestParam("couponIssuanceId") Long couponIssuanceId, RedirectAttributes redirectAttributes) {
        couponService.useCoupon(couponIssuanceId);

        return ResponseEntity.status(HttpStatus.OK).body("쿠폰이 성공적으로 사용되었습니다.");
    }
}
