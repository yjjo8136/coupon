package com.coupon.coupon.service;

import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
import com.coupon.coupon.domain.User;
import com.coupon.coupon.repository.CouponIssuanceRepository;
import com.coupon.coupon.repository.CouponRepository;
import com.coupon.coupon.repository.UserRepository;
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
    private final RedissonClient redissonClient;

    @Autowired
    public CouponService(CouponRepository couponRepository,
                         UserRepository userRepository,
                         CouponIssuanceRepository couponIssuanceRepository, RedissonClient redissonClient) {
        this.couponRepository = couponRepository;
        this.userRepository = userRepository;
        this.couponIssuanceRepository = couponIssuanceRepository;
        this.redissonClient = redissonClient;
    }

    // 쿠폰 생성
    public void createCoupon(Coupon coupon) {
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

        RLock lock = redissonClient.getLock("couponLock:" + couponId);
        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                System.out.println("redisson getLock Timeout");
                return false;
            }

            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new RuntimeException("쿠폰을 찾을 수 없습니다."));

            if (coupon.getRemainingQuantity() <= 0) {
                return false;
            }

            // 쿠폰 중복 발급 여부 확인 (동시성 테스트를 위해 잠시 주석처리)
            //List<CouponIssuance> existingIssuances = couponIssuanceRepository.findByCouponIdAndUserId(couponId, userId);
            //if (!existingIssuances.isEmpty()) {
            //    return false; // 이미 발급된 쿠폰이 있는 경우
            //}

            coupon.setRemainingQuantity(coupon.getRemainingQuantity() - 1);
            couponRepository.saveAndFlush(coupon);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            CouponIssuance issuance = new CouponIssuance();
            issuance.setCouponId(coupon);
            issuance.setUserId(user);
            issuance.setIssuanceDate(LocalDateTime.now().toString());
            issuance.setStatus("issued");
            issuance.setUsedAt(null);

            couponIssuanceRepository.save(issuance);

            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
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
