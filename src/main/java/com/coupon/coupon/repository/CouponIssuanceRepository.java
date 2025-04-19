package com.coupon.coupon.repository;

import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
import com.coupon.coupon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponIssuanceRepository {

    CouponIssuance save(CouponIssuance couponIssuance); // 쿠폰 발급 저장

    List<CouponIssuance> findByUserId(Long userId); // 사용자 ID로 쿠폰 발급 조회

    CouponIssuance findByIssuanceId(Long couponIssuanceId);

    List<CouponIssuance> findByCouponIdAndUserId(Long couponId, Long userId);
}
