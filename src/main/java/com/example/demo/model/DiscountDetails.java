package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Embeddable
public class DiscountDetails {
    private Integer threshold; // For cart-wise coupons
    private Integer discount;   // Discount percentage or amount

    @ElementCollection // To store lists of products
    private List<ProductQuantity> buyProducts; // For BxGy coupons

    @ElementCollection
    private List<ProductQuantity> getProducts; // For BxGy coupons

    private Integer repetitionLimit; // For BxGy coupons

    private Integer productId;  // For product-wise discounts
    private Integer productDiscount; // Discount for the specific product


    // Getters and Setters

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(Integer productDiscount) {
        this.productDiscount = productDiscount;
    }

    
    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public List<ProductQuantity> getBuyProducts() {
        return buyProducts;
    }

    public void setBuyProducts(List<ProductQuantity> buyProducts) {
        this.buyProducts = buyProducts;
    }

    public List<ProductQuantity> getGetProducts() {
        return getProducts;
    }

    public void setGetProducts(List<ProductQuantity> getProducts) {
        this.getProducts = getProducts;
    }

    public Integer getRepetitionLimit() {
        return repetitionLimit;
    }

    public void setRepetitionLimit(Integer repetitionLimit) {
        this.repetitionLimit = repetitionLimit;
    }
}
