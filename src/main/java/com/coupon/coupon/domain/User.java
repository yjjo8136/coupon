package com.coupon.coupon.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String name;

    public void setId(Long id) {
        this.user_id = id;
    }
    public Long getId() {
        return user_id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }
    public Long getUserId() {
        return user_id;
    }
    public void setUserName(String name) {
        this.name = name;
    }
    public String getUserName() {
        return name;
    }


}
