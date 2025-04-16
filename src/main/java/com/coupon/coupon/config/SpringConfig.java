package com.coupon.coupon.config;

import com.coupon.coupon.repository.*;
import com.coupon.coupon.service.CouponService;
import jakarta.persistence.EntityManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private final EntityManager em;

    @Autowired
    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public CouponRepository couponRepository() {
        return new JpaCouponRepository(em);
    }

    @Bean
    public CouponService couponService() {
        return new CouponService(couponRepository(), userRepository(), couponIssuanceRepository(), redissonClient());
    }

    @Bean
    public UserRepository userRepository() {
        return new JpaUserRepository(em);
    }

    private CouponIssuanceRepository couponIssuanceRepository() {
        return new JpaCouponIssuanceRepository(em);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        return Redisson.create(config);
    }

}
