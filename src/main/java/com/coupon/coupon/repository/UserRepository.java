package com.coupon.coupon.repository;

import com.coupon.coupon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    // 이름을 기준으로 회원을 찾기 위한 메서드

    User save(User user);
    User findByName(String name);
    Optional<User> findById(Long id);
}
