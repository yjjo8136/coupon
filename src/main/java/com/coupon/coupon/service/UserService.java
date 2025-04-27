package com.coupon.coupon.service;

import com.coupon.coupon.domain.User;
import com.coupon.coupon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByName(String name) {
        return userRepository.findByName(name);
    }
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

}
