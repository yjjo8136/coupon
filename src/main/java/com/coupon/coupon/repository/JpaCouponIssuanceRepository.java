package com.coupon.coupon.repository;

import com.coupon.coupon.domain.CouponIssuance;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaCouponIssuanceRepository implements CouponIssuanceRepository {
    private final EntityManager em;
    public JpaCouponIssuanceRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public CouponIssuance save(CouponIssuance couponIssuance) {
        em.persist(couponIssuance);
        return couponIssuance;
    }

    @Override
    public List<CouponIssuance> findByUserId(Long userId) {
        return em.createQuery("select ci from CouponIssuance ci where ci.user.id = :userId", CouponIssuance.class)
                .setParameter("userId", userId)
                .getResultList();
    }


    @Override
    public CouponIssuance findByIssuanceId(Long couponId) {
        return em.createQuery("select ci from CouponIssuance ci where ci.id = :couponId", CouponIssuance.class)
                .setParameter("couponId", couponId)
                .getSingleResult();
    }

    @Override
    public List<CouponIssuance> findByCouponIdAndUserId(Long couponId, Long userId) {
        return em.createQuery("select ci from CouponIssuance ci where ci.coupon.id = :couponId and ci.user_id.id = :userId", CouponIssuance.class)
                .setParameter("couponId", couponId)
                .setParameter("userId", userId)
                .getResultList();
    }

}
