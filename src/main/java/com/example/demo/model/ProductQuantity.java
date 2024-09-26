package com.example.demo.model;
import jakarta.persistence.*;

@Embeddable
public class ProductQuantity {
    private Long productId; // The product ID
    private Integer quantity; // The quantity of the product

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}