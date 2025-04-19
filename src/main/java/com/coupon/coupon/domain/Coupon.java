package com.coupon.coupon.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "coupon")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coupon_id;
    private String expiration_date;
    private String coupon_type;
    private Long total_quantity;
    private Long remaining_quantity;


    public void setCouponId(Long coupon_id) {
        this.coupon_id = coupon_id;
    }
    public Long getCouponId() {
        return coupon_id;
    }
    public void setExpirationDate(String expiration_date) {
        this.expiration_date = expiration_date;
    }
    public String getExpirationDate() {
        return expiration_date;
    }
    public void setCouponType(String coupon_type) {
        this.coupon_type = coupon_type;
    }
    public String getCouponType() {
        return coupon_type;
    }
    public void setTotalQuantity(Long total_quantity) {
        this.total_quantity = total_quantity;
    }
    public Long getTotalQuantity() {
        return total_quantity;
    }
    public void setRemainingQuantity(Long remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
    }
    public Long getRemainingQuantity() {
        return remaining_quantity;
    }

}
