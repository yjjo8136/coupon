package com.coupon.coupon.service;

import com.coupon.coupon.common.CouponIssuanceStatus;
import com.coupon.coupon.common.SessionConstant;
import com.coupon.coupon.common.UserRole;
import com.coupon.coupon.domain.*;
import com.coupon.coupon.exception.CouponErrorCode;
import com.coupon.coupon.exception.CouponException;
import com.coupon.coupon.repository.CouponIssuanceRepository;
import com.coupon.coupon.repository.CouponRepository;
import com.coupon.coupon.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final CouponIssuanceRepository couponIssuanceRepository;
    private final HttpSession httpSession;
    private final RedissonClient redissonClient;


    @Autowired
    public CouponService(CouponRepository couponRepository,
                         UserRepository userRepository,
                         CouponIssuanceRepository couponIssuanceRepository,
                         HttpSession httpSession,
                         RedissonClient redissonClient) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.couponIssuanceRepository = couponIssuanceRepository;
        this.httpSession = httpSession;
        this.redissonClient = redissonClient;
    }

    // 쿠폰 생성
    @Transactional
    public void createCoupon(Coupon coupon) {
        // 관리자 권한 확인
        User currentUser = (User) httpSession.getAttribute(SessionConstant.CURRENT_USER);
        if (currentUser == null || !UserRole.ADMIN.equals(currentUser.getRole())) {
            throw new CouponException(CouponErrorCode.ACCESS_DENIED);
        }
        couponRepository.save(coupon);
    }

    // 쿠폰 목록 조회
    @Transactional(readOnly = true)
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    @Transactional
    // 쿠폰 사용
    public void useCoupon(Long couponIssuanceId) {

        // 쿠폰 발급 기록 존재 여부 확인
        CouponIssuance couponIssuance = couponIssuanceRepository.findByIssuanceId(couponIssuanceId);
        if (couponIssuance == null) {
            throw new CouponException(CouponErrorCode.COUPON_ISSUANCE_NOT_FOUND);
        }

        // 쿠폰 사용 여부 확인
        if (couponIssuance.getStatus().equals(CouponIssuanceStatus.USED)) {
            throw new CouponException(CouponErrorCode.COUPON_ISSUANCE_ALREADY_USED);
        }

        // 쿠폰 만료 여부 확인
        //Coupon coupon = couponIssuance.getCoupon();
        //LocalDateTime expireDateTime = LocalDateTime.parse(coupon.getExpirationDate());
        //if (expireDateTime.isBefore(LocalDateTime.now())) {
        //    throw new CustomException(CustomErrorCode.COUPON_EXPIRED);
        //}

        // 쿠폰 사용 로직
        couponIssuance.used();
        couponIssuanceRepository.save(couponIssuance);
    }

    @Transactional
    public boolean issueCoupon(Long couponId, Long userId) {

        RAtomicLong counter = redissonClient.getAtomicLong("coupon:remaining:" + couponId);

        if (!counter.isExists()) {
            long dbRemaining = couponRepository.findById(couponId)
                    .orElseThrow(() -> new CouponException(CouponErrorCode.COUPON_NOT_FOUND))
                    .getRemainingQuantity();
            counter.set(dbRemaining);
        }

        // Redis에서 쿠폰 재고 1 감소
        long remainAfterDecr = counter.decrementAndGet();
        if (remainAfterDecr < 0) {
            counter.incrementAndGet();
            return false;
        }

        // DB에서 쿠폰 재고 1 감소
        couponRepository.decrementRemainingQuantity(couponId);

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CouponException(CouponErrorCode.USER_NOT_FOUND));

        Coupon couponProxy = couponRepository.getReference(couponId);

        // 발급 기록 생성 및 저장
        CouponIssuance issuance = new CouponIssuance(couponProxy, user);
        couponIssuanceRepository.save(issuance);

        return true;
    }

    @Transactional(readOnly = true)
    public List<CouponIssuance> getMyCoupons(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CouponException(CouponErrorCode.USER_NOT_FOUND));
        List<CouponIssuance> couponIssuance = couponIssuanceRepository.findByUserId(user.getId());
        return couponIssuance;
    }

}
