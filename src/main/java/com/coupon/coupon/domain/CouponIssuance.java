package com.coupon.coupon.domain;

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
    private Coupon coupon_id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;
    private String issuance_date;
    private LocalDateTime used_at;
    private String status;

    public void setId(Long id) {
        this.coupon_issuance_id = id;
    }
    public Long getId() {
        return coupon_issuance_id;
    }
    public void setCouponId(Coupon coupon_id) {
        this.coupon_id = coupon_id;
    }
    public Coupon getCouponId() {
        return coupon_id;
    }
    public void setUserId(User user_id) {
        this.user_id = user_id;
    }
    public User getUserId() {
        return user_id;
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
    public void setStatus(String status) {
        this.status = status;
        this.used_at = LocalDateTime.now();
    }

    public void used() {
        this.status = "used";
        this.used_at = LocalDateTime.now();
    }
    public String getStatus() {
        return status;
    }


}
