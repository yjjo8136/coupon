package com.coupon.coupon;

import com.coupon.coupon.domain.Coupon;
import com.coupon.coupon.domain.CouponIssuance;
import com.coupon.coupon.repository.CouponRepository;
import com.coupon.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportAutoConfiguration(exclude = {ManagementWebSecurityAutoConfiguration.class})
class CouponApplicationTests {

	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponRepository couponRepository;

	@Test
	void 동시에_100명이_쿠폰을_발급_받으면_잔여_쿠폰_개수가_100개_줄어든다() throws InterruptedException {

		Coupon coupon = new Coupon();
		coupon.setCouponType("치킨");              // 쿠폰 종류 지정
		coupon.setExpirationDate("2025-12-31");     // 유효기간 설정
		coupon.setTotalQuantity(100L);              // 총 발급 가능 수량 100
		coupon.setRemainingQuantity(100L);          // 초기 남은 수량 100

		couponService.createCoupon(coupon);		// 쿠폰 생성
		Long couponId = coupon.getId();

		ExecutorService executorService = Executors.newFixedThreadPool(100);
		CountDownLatch countDownLatch = new CountDownLatch(100);

		for (int i = 0; i < 100; i++) {
			executorService.submit(() -> {
				try {
					couponService.issueCoupon(couponId, 1L);	// 쿠폰 발급
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		Coupon actual = couponRepository.findById(couponId).orElseThrow();
		assertThat(actual.getRemainingQuantity()).isZero();
	}


}

