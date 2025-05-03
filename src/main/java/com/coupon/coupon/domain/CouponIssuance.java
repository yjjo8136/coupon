package com.coupon.coupon.domain;

import com.coupon.coupon.common.CouponIssuanceStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_issuance")
public class CouponIssuance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coupon_issuance_id;
    @ManyToOne
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String issuance_date;
    private LocalDateTime used_at;
    @Enumerated(EnumType.STRING)
    private CouponIssuanceStatus status;

    // Hibernate는 무조건 파라미터가 없는 public 생성자가 있어야 한다.
    public CouponIssuance() {
    }

    public CouponIssuance(Coupon coupon, User user) {
        this.coupon = coupon;
        this.user = user;
        this.issuance_date = LocalDateTime.now().toString();
        this.used_at = null;
        this.status = CouponIssuanceStatus.ISSUED;
    }



    public void setId(Long id) {
        this.coupon_issuance_id = id;
    }
    public Long getId() {
        return coupon_issuance_id;
    }
    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }
    public Coupon getCoupon() {
        return coupon;
    }
    public void setUserId(User user) {
        this.user = user;
    }
    public User getUserId() {
        return user;
    }
    public void setIssuanceDate(String issuance_date) {
        this.issuance_date = issuance_date;
    }
    public String getIssuanceDate() {
        return issuance_date;
    }
    public void setUsedAt(LocalDateTime used_at) {
        this.used_at = used_at;
    }
    public LocalDateTime getUsedAt() {
        return used_at;
    }
    public void setStatus(CouponIssuanceStatus status) {
        this.status = status;
        this.used_at = LocalDateTime.now();
    }
    public CouponIssuanceStatus getStatus() {
        return status;
    }

    public void used() {
        this.status = CouponIssuanceStatus.USED;
        this.used_at = LocalDateTime.now();
    }

}
