package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.demo.model.CartItem;
import com.example.demo.model.CartItemResponse;
import com.example.demo.model.Coupon;
import com.example.demo.model.CouponResponse;
import com.example.demo.model.DiscountDetails;
import com.example.demo.model.ProductQuantity;
import com.example.demo.model.Cart;
import com.example.demo.repository.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;



@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon) {
        System.out.println(coupon);
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    // public boolean couponExists(Long id) {
    //     return couponRepository.existsById(id);
    // }
    
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

   public List<CouponResponse> getApplicableCoupons(Cart cart) {
        List<Coupon> allCoupons = couponRepository.findAll();
        List<CouponResponse> applicableCoupons = new ArrayList<>();
        
        double cartTotal = cart.getItems().stream()
                                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                                .sum();
        
        for (Coupon coupon : allCoupons) {
            DiscountDetails details = coupon.getDetails();
            
            // Cart-Wise Coupon Logic
            if (coupon.getType().equals("cart-wise") && cartTotal >= details.getThreshold()) {
                double discount = cartTotal * (details.getDiscount() / 100.0);
                applicableCoupons.add(new CouponResponse(coupon.getId(), "cart-wise", discount));
            } 
            
            // Product-Wise Coupon Logic
            else if (coupon.getType().equals("product-wise")) {
                double discount = calculateProductWiseDiscount(coupon, cart);
                if (discount > 0) {
                    applicableCoupons.add(new CouponResponse(coupon.getId(), "product-wise", discount));
                }
            }

            // BxGy Coupon Logic
            else if (coupon.getType().equals("bxgy")) {
                double discount = calculateBxGyDiscount(coupon, cart);
                if (discount > 0) {
                    applicableCoupons.add(new CouponResponse(coupon.getId(), "bxgy", discount));
                }
            }
            
            
        }
        
        return applicableCoupons;
    }

    private double calculateProductWiseDiscount(Coupon coupon, Cart cart) {
        DiscountDetails details = coupon.getDetails();
        double discount = 0;
        
        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(details.getProductId())) {
                // Applying the discount for  product-specific item
                discount = item.getPrice() * item.getQuantity() * (details.getProductDiscount() / 100.0);
                break; // ony for 1 product
            }
        }
        
        return discount;
    }

    private double calculateBxGyDiscount(Coupon coupon, Cart cart) {
    
        int repetitions =  coupon.getDetails().getRepetitionLimit();
    
        double discount = 0;
    
        // Calculate discount based on get products
        if (repetitions > 0) {
            for (ProductQuantity getProduct : coupon.getDetails().getGetProducts()) {
                Optional<CartItem> getProductItem = cart.getItems().stream()
                                                        .filter(item -> item.getProductId().equals(getProduct.getProductId()))
                                                        .findFirst();
                if (getProductItem.isPresent()) {
                    
                    int availableQuantity = getProductItem.get().getQuantity();
                    int totalFreeQuantity = getProduct.getQuantity() * repetitions; 
                    int freeQuantityToConsider = Math.min(totalFreeQuantity, availableQuantity);
                    
                    // Calculate the discount based on the total free quantity allowed
                    discount += getProductItem.get().getPrice() * freeQuantityToConsider; // Adjust discount calculation
                    System.out.println("Repetitions: " + repetitions);
                    System.out.println("Total free quantity for product " + getProduct.getProductId() + ": " + totalFreeQuantity);
                    System.out.println("Available quantity in cart for product " + getProduct.getProductId() + ": " + availableQuantity);
                    System.out.println("Discount calculated for product " + getProduct.getProductId() + ": " + (getProductItem.get().getPrice() * freeQuantityToConsider));

                }
            }
        }
    
        return discount;
    }

    public Map<String, Object> applyCouponToCart(Long couponId, Cart cart) {
        Optional<Coupon> couponOpt = couponRepository.findById(couponId);
    
        if (!couponOpt.isPresent()) {
            return null;
        }
    
        Coupon coupon = couponOpt.get();
        DiscountDetails details = coupon.getDetails();
    
        double totalCartPrice = 0.0;
        double totalCartDiscount = 0.0;
    
        List<CartItemResponse> updatedItems = new ArrayList<>();
    
        
        for (CartItem item : cart.getItems()) {
            totalCartPrice += item.getQuantity() * item.getPrice();
        }
    
        Map<Long, Integer> additionalQuantities = new HashMap<>();
        Map<Long, Double> itemSpecificDiscounts = new HashMap<>();
    
        
        for (CartItem item : cart.getItems()) {
            CartItemResponse itemResponse = new CartItemResponse();
            itemResponse.setProductId(item.getProductId());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            double itemDiscount = 0;
    
            switch (coupon.getType()) {
                case "cart-wise":
                    if (totalCartPrice >= details.getThreshold()) {
                        itemDiscount = (item.getPrice() * details.getDiscount() / 100) * item.getQuantity();
                    }
                    break;
    
                case "product-wise":
            
                    if (details.getProductId().equals(item.getProductId())) {
                        itemDiscount = (item.getPrice() * details.getProductDiscount() / 100) * item.getQuantity();
                    }
                    break;
    
                case "bxgy":
        
                    for (ProductQuantity buyProduct : details.getBuyProducts()) {
                        if (buyProduct.getProductId().equals(item.getProductId()) && item.getQuantity() > buyProduct.getQuantity()) {
                           
                            int numberOfTimesOfferApplies = item.getQuantity() / buyProduct.getQuantity();
                            
                            for (ProductQuantity getProduct : details.getGetProducts()) {
                                CartItem freeItem = findCartItemById(cart, getProduct.getProductId());
                                if (freeItem != null) {
                                
                                    int freeQuantity = Math.min(numberOfTimesOfferApplies, details.getRepetitionLimit()) * getProduct.getQuantity();
                                    
                                    //System.out.println("Free quantity: " + freeQuantity);
                                    //System.out.println("Free product Id: " + freeItem.getProductId());
    
                                    additionalQuantities.put(getProduct.getProductId(),
                                            additionalQuantities.getOrDefault(getProduct.getProductId(), 0) + freeQuantity);
    
                                    double freeItemDiscount = freeQuantity * freeItem.getPrice();
                                   
                                   
                                    itemSpecificDiscounts.put(freeItem.getProductId(),
                                            itemSpecificDiscounts.getOrDefault(freeItem.getProductId(), 0.0) + freeItemDiscount);
                                    totalCartDiscount += freeItemDiscount;
                                }
                            }
                        }
                    }
                    break;
            }
            totalCartDiscount += itemDiscount;
            itemResponse.setTotalDiscount(itemDiscount);
            updatedItems.add(itemResponse);
        }
    
       
        for (Map.Entry<Long, Integer> entry : additionalQuantities.entrySet()) {
            CartItem freeItem = findCartItemById(cart, entry.getKey());
            if (freeItem != null) {
                freeItem.setQuantity(freeItem.getQuantity() + entry.getValue());
            }
        }
    
        for (CartItemResponse itemResponse : updatedItems) {
            if (itemSpecificDiscounts.containsKey(itemResponse.getProductId())) {
                itemResponse.setTotalDiscount(itemSpecificDiscounts.get(itemResponse.getProductId()));
            }
        }
    
        double finalPrice = totalCartPrice - totalCartDiscount;
        Map<String, Object> updatedCart = new HashMap<>();
        Map<String, Object> cartResponse = new HashMap<>();
        cartResponse.put("items", updatedItems);
        cartResponse.put("total_price", totalCartPrice);
        cartResponse.put("total_discount", totalCartDiscount);
        cartResponse.put("final_price", finalPrice);
    
        updatedCart.put("updated_cart", cartResponse);
    
        return updatedCart;
    }
    
    private CartItem findCartItemById(Cart cart, Long productId) {
        return cart.getItems().stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .orElse(null);
    }
}

