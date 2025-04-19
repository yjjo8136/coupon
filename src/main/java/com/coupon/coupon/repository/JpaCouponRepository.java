package com.coupon.coupon.repository;

import com.coupon.coupon.domain.Coupon;
import jakarta.persistence.EntityManager;
import org.redisson.api.RAtomicLong;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class JpaCouponRepository implements CouponRepository{
    private final EntityManager em;
    public JpaCouponRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public List<Coupon> findAll() {
        return em.createQuery("select c from Coupon c", Coupon.class)
                .getResultList(); // 모든 쿠폰 조회
    }
    @Override
    public Coupon save(Coupon coupon) {
        em.persist(coupon);
        return coupon;
    }
    @Override
    public void delete(Coupon coupon) {
    }
    @Override
    public Optional<Coupon> findById(Long id) {
        return em.createQuery("select c from Coupon c where c.id = :id", Coupon.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findAny(); // 쿠폰 ID로 쿠폰 조회
    }
    @Override
    public List<Coupon> findByUserId(Long userId) {
        return em.createQuery("select c from Coupon c where c.userId = :userId", Coupon.class)
                .setParameter("userId", userId)
                .getResultList(); // 사용자 ID로 쿠폰 조회
    }

    @Override
    public void saveAndFlush(Coupon coupon) {
        em.persist(coupon);
        em.flush(); // 쿠폰 저장 후 즉시 반영
    }

    @Override
    public long decrementRemainingQuantity(Long couponId) {
        return em.createQuery("UPDATE Coupon c SET c.remainingQuantity = c.remainingQuantity - 1 " + "WHERE c.id = :id")
                .setParameter("id", couponId)
                .executeUpdate();
    }

}
