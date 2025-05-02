package com.coupon.coupon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                )
                .logout(logout -> logout.disable()) // Spring Security의 기본 로그아웃을 비활성화하여 /logout GET 요청을 가로채지 않도록 설정
                .csrf(csrf -> csrf.disable());
        // CSRF 보호를 비활성화합니다. (REST API에서는 CSRF 보호가 필요하지 않을 수 있습니다.)
        return http.build();
    }
}
