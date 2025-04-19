package com.coupon.coupon.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 가장 먼저 실행
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    public DistributedLockAspect(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
/*
    @Around("execution(public boolean com.coupon.coupon..CouponService.issueCoupon(Long, Long))")
    public Object aroundIssueCoupon(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 인자에서 couponId 꺼내기
        Object[] args = joinPoint.getArgs();
        Long couponId = (Long) args[0];

        // 분산락 키 생성
        String lockKey = "couponLock:" + couponId;
        RLock lock = redissonClient.getLock(lockKey);

        // 락 획득 시도 (대기 10초, lease 1초)
        boolean acquired = lock.tryLock(10, 1, TimeUnit.SECONDS);
        // 락 획득 실패 시 재시도
        while (!acquired) {
            acquired = lock.tryLock(10, 1, TimeUnit.SECONDS);
        }

        try {
            // 비즈니스 메서드 실행
            return joinPoint.proceed();
        } finally {
            // 락 해제
            lock.unlock();
        }
    }

 */
}
