package com.coupon.coupon.service;

import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
import com.coupon.coupon.domain.User;
import com.coupon.coupon.repository.CouponIssuanceRepository;
import com.coupon.coupon.repository.CouponRepository;
import com.coupon.coupon.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
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
    public void createCoupon(Coupon coupon) {
        // 관리자 권한 확인
        User currentUser = (User) httpSession.getAttribute("currentUser");
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("관리자만 쿠폰을 생성할 수 있습니다.");
        }
        couponRepository.save(coupon);
    }

    // 쿠폰 목록 조회
    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    // 쿠폰 사용
    public void useCoupon(CouponIssuance couponIssuance) {
        // 쿠폰 사용 여부 확인
        if (couponIssuance.getStatus().equals("used")) {
            throw new RuntimeException("이미 사용된 쿠폰입니다.");
        }

        // 쿠폰 사용 로직
        couponIssuance.setStatus("used");
        couponIssuance.setUsedAt(LocalDateTime.now());
        couponIssuanceRepository.save(couponIssuance);
    }


    public boolean issueCoupon(Long couponId, Long userId) {

        RAtomicLong counter = redissonClient.getAtomicLong("coupon:remaining:" + couponId);

        if (!counter.isExists()) {
            long dbRemaining = couponRepository.findById(couponId)
                    .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다. ID=" + couponId))
                    .getRemainingQuantity();
            counter.set(dbRemaining);
        }

        long remainAfterDecr = counter.decrementAndGet();
        if (remainAfterDecr < 0) {
            counter.incrementAndGet();
            return false;
        }

        // DB 업데이트
        int updated = couponRepository.decrementRemainingQuantity(couponId);
        if (updated == 0) {
            counter.incrementAndGet();
            throw new RuntimeException("쿠폰이 존재하지 않습니다. ID=" + couponId);
        }

        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID=" + userId));


        Coupon couponProxy = couponRepository.getReference(couponId);

        // 발급 기록 생성 및 저장
        CouponIssuance issuance = new CouponIssuance();
        issuance.setCouponId(couponProxy);
        issuance.setUserId(user);
        issuance.setIssuanceDate(LocalDateTime.now().toString());
        issuance.setStatus("issued");
        issuance.setUsedAt(null);
        couponIssuanceRepository.save(issuance);

        return true;
    }


    public List<CouponIssuance> getMyCoupons(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        List<CouponIssuance> couponIssuance = couponIssuanceRepository.findByUserId(user.getId());
        return (List<CouponIssuance>) new ArrayList<CouponIssuance>(couponIssuance);
    }


    public CouponIssuance getCouponIssuanceById(Long couponIssuanceId) {
        return couponIssuanceRepository.findByIssuanceId(couponIssuanceId);
    }
}
