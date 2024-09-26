package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @Embedded  
    private DiscountDetails  details;


    public DiscountDetails  getDetails() {
        return details;
    }

    public void setDetails(DiscountDetails  details) {
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
