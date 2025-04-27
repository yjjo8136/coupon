package com.coupon.coupon.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String name;
    @Enumerated(EnumType.STRING)
    private UserRole role;


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
    public void setRole(UserRole role) {
        this.role = role;
    }
    public UserRole getRole() {
        return role;
    }

}
