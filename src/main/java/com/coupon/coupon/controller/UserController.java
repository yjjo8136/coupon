package com.coupon.coupon.controller;

import com.coupon.coupon.common.SessionConstant;
import com.coupon.coupon.domain.User;
import com.coupon.coupon.repository.UserRepository;
import com.coupon.coupon.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 처리: 이름만 입력받아서 회원 생성
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@ModelAttribute User user, HttpSession session) {
        User savedUser = userService.signUp(user);
        session.setAttribute(SessionConstant.CURRENT_USER, savedUser);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    // 로그인 처리: 입력된 이름으로 회원 조회
    @PostMapping("/login")
    public ResponseEntity<String> login(@ModelAttribute User user, HttpSession session) {
        User loginUser = userService.login(user);
        session.setAttribute(SessionConstant.CURRENT_USER, loginUser);
        return ResponseEntity.ok("로그인되었습니다.");
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
}
