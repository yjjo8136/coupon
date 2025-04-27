package com.coupon.coupon.controller;

import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
import com.coupon.coupon.service.CouponService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CouponController {
    @Autowired
    private CouponService couponService;

    @GetMapping("/")
    public String index() {
        return "redirect:/couponList";
    }

    // 쿠폰 목록 페이지로 이동
    @GetMapping("/couponList")
    public String showCouponList(Model model) {
        List<Coupon> coupons = couponService.getAllCoupons();
        model.addAttribute("coupons", coupons);
        // couponList.html 뷰를 반환
        return "couponList";
    }

    // 쿠폰 추가 폼 페이지로 이동
    @GetMapping("/addCoupon")
    public String showCouponAddForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        return "addCoupon";
    }

    // 쿠폰 추가 폼 제출 처리
    @PostMapping("/addCoupon")
    public String addCoupon(@ModelAttribute Coupon coupon) {
        couponService.createCoupon(coupon);
        return "redirect:/couponList";
    }

    @PostMapping("/issueCoupon")
    public String issueCoupon(@RequestParam("couponId") Long couponId, HttpSession session, RedirectAttributes redirectAttributes) {
        // 세션에서 현재 로그인한 사용자 정보 가져오기
        Object currentObj = session.getAttribute("currentUser");

        // 로그인한 사용자의 ID를 가져옴
        Long userId = ((com.coupon.coupon.domain.User) currentObj).getId();
        boolean issued = couponService.issueCoupon(couponId, userId);
        if (issued) {
            redirectAttributes.addFlashAttribute("message", "쿠폰이 성공적으로 발급되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("message", "이미 해당 쿠폰을 발급 받았거나 재고가 부족합니다.");
        }
        return "redirect:/couponList";
    }

    // 사용자가 발급받은 쿠폰 목록 페이지로 이동
    @GetMapping("/myCoupons")
    public String showMyCoupons(HttpSession session, Model model) {
        // 세션에서 현재 로그인한 사용자 정보 가져오기
        Object currentObj = session.getAttribute("currentUser");
        // 로그인한 사용자의 ID를 가져옴
        Long userId = ((com.coupon.coupon.domain.User) currentObj).getId();
        List<CouponIssuance> myCoupons = couponService.getMyCoupons(userId);
        model.addAttribute("myCoupons", myCoupons);
        return "myCoupons";
    }

    // 쿠폰 사용 처리
    @PostMapping("/useCoupon")
    public String useCoupon(@RequestParam("couponIssuanceId") Long couponIssuanceId, RedirectAttributes redirectAttributes) {
        CouponIssuance couponIssuance = couponService.getCouponIssuanceById(couponIssuanceId);
        if (couponIssuance != null) {
            couponService.useCoupon(couponIssuance);
            redirectAttributes.addFlashAttribute("message", "쿠폰이 성공적으로 사용되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "쿠폰 사용에 실패했습니다.");
        }
        return "redirect:/myCoupons";
    }
}
