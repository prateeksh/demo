package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Coupon; 

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}

