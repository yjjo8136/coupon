package com.coupon.coupon.repository;

import com.coupon.coupon.domain.Coupon;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository {

    List<Coupon> findAll(); // 모든 쿠폰 조회
    Coupon save(Coupon coupon); // 쿠폰 저장
    void delete(Coupon coupon); // 쿠폰 삭제
    Optional<Coupon> findById(Long id); // 쿠폰 ID로 쿠폰 조회
    List<Coupon> findByUserId(Long userId); // 사용자 ID로 쿠폰 조회

    void saveAndFlush(Coupon coupon);

    int decrementRemainingQuantity(Long couponId);

    Coupon getReference(Long couponId);

}
