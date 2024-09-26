package com.example.demo.model;

public class CouponResponse {
    private Long couponId;
    private String type;
    private double discount;

    public CouponResponse(Long couponId, String type, double discount) {
        this.couponId = couponId;
        this.type = type;
        this.discount = discount;
    }

    // Getters and Setters
    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getType() {
        return type;
    }

    public void setId(String type) {
        this.type = type;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}
