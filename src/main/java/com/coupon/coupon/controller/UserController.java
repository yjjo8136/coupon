package com.coupon.coupon.controller;

import com.coupon.coupon.domain.User;
import com.coupon.coupon.repository.UserRepository;
import com.coupon.coupon.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입 폼 페이지
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";  // signup.html 템플릿
    }

    // 회원가입 처리: 이름만 입력받아서 회원을 생성
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        if (userService.findByName(user.getName()) != null) {
            redirectAttributes.addFlashAttribute("error", "이미 존재하는 사용자입니다.");
            return "redirect:/signup";
        }
        User savedUser = userService.save(user);
        session.setAttribute("currentUser", savedUser);
        return "redirect:/couponList";
    }

    // 로그인 폼 페이지
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";  // login.html 템플릿
    }

    // 로그인 처리: 입력된 이름으로 회원 조회
    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User existingUser = userService.findByName(user.getName());
        if (existingUser == null) {
            redirectAttributes.addFlashAttribute("error", "해당 사용자가 존재하지 않습니다.");
            return "redirect:/login";
        }
        session.setAttribute("currentUser", existingUser);
        return "redirect:/couponList";
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/couponList";
    }
}
