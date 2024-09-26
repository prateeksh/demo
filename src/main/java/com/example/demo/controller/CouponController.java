package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.CouponService;
import com.example.demo.model.Coupon;
import com.example.demo.model.CouponResponse;
import com.example.demo.model.Cart;
import com.example.demo.model.CartRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @PostMapping
    public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon) {
        System.out.println(coupon);
        Coupon createdCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(createdCoupon);
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = couponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCouponById(@PathVariable Long id) {
        Coupon coupon = couponService.getCouponById(id);
        if (coupon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Coupon not found"));
        }
        return ResponseEntity.ok(coupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCoupon(@PathVariable Long id) {
        // Check if the coupon exists
        if (couponService.getCouponById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Coupon not found"));
        }
        
        couponService.deleteCoupon(id); // Proceed to delete if available
        Map<String, String> responseMessage = new HashMap<>();
        responseMessage.put("message", "Coupon deleted successfully");

        return ResponseEntity.ok(responseMessage); // Return 200 with the success message
    }

    @PostMapping("/applicable-coupons")
    public ResponseEntity<Map<String, Object>> getApplicableCoupons(@RequestBody CartRequest cartRequest) {
        Cart cart = cartRequest.getCart();
        List<CouponResponse> applicableCoupons = couponService.getApplicableCoupons(cart);
        Map<String, Object> response = new HashMap<>();
        response.put("applicable_coupons", applicableCoupons);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply-coupon/{id}")
    public ResponseEntity<Map<String, Object>> applyCoupon(@PathVariable Long id, @RequestBody CartRequest cartRequest) {
        Cart cart = cartRequest.getCart();
        Map<String, Object> updatedCart = couponService.applyCouponToCart(id, cart);
        
        //System.out.println(id);
        //System.out.println(cart);
        
        if (updatedCart == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Coupon not found"));
        }

        return ResponseEntity.ok(updatedCart);
    }
}
