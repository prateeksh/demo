Overview

An e-commerce platform's coupon application mechanism is being implemented by this project. The shopping cart system facilitates the implementation of various discount schemes, such as product-specific, buy-X-get-Y free, and cart-wise discounts. Here I've used MySql DB to store the details such as product, discount conditions for BxGy case and other condition required for product specific and cart wise discounts. In this code the table has not been created for cart as a list is being used instead.

Implemented Cases

1. Cart-Wise Discounts
Description: The amount of the discount is determined by the cart's total cost.
Logic: A percentage discount is applied to the total amount if the cart price exceeds a predetermined threshold.

2. Product-Specific Discounts
Description: Particular savings on each item in the cart.
Logic: Based on predetermined guidelines, a discount % is applied to a certain product when it is added to the cart.

3. Free Discounts on Buy X, Get Y (BxGy)
Description: When customers buy a certain amount of other products, they can obtain free items.
Logic: The consumer must fulfill the purchase conditions for each of the listed products in order for the offer to be valid. The designated quantity of the complimentary item is added to the cart if the requirements are satisfied.


Unimplemented Cases

1. Discounts for Combinations
Discounts that can be used in conjunction with other coupon kinds (such as a product-wise discount combined with a cart-wise discount).
Reason: Because it could make the discount calculation more difficult and result in overlapping discounts, the current implementation does not permit the application of multiple discount kinds at the same time.

2. Coupon Expiration Dates
Coupons featuring start and end dates are described.
Reason: More date-handling logic would be needed if the existing solution did not validate coupon expiration.

3. Coupon Usage Restrictions
Description: Restricting how many times a client can use a coupon.
Reason: Since coupon usage by client must be tracked in order to implement this functionality, it is not currently supported.


Limitations

Single Coupon Application: Only one coupon may be applied per transaction under the current system. It might be possible to stack numerous coupons in future versions.

1. No Support for Nested Promotions: Complex promotions that allow for the simultaneous application of numerous discounts are not taken into account by the system.

2. Manual Input Needed: A lot of the logic depends on manually entering product and coupon data, which raises the possibility of human error.

3. Performance with Big Carts: Because of the quantity of checks and iterations required, performance may suffer with large shopping carts.


Assumptions

Valid Product IDs: All product IDs in the basket and coupon details are presumed to be genuine and match the real products that are kept in stock.

Consistent Pricing: At the time of applying a coupon, prices are retrieved from a trustworthy source and are presumed to stay consistent.

No Stock constraints: The implementation makes the assumption that there are enough products available to satisfy the buy-X-get-Y deals without taking into account the products' stock constraints.

Coupon Format: All coupon data is presumed to adhere to the standard format; any deviations could result in mistakes or strange behavior.

Discount kinds: A predetermined set of discount kinds are included into the system. The current logic will need to be updated for any new types.


Conclusion

The coupon application system's current implementation, restrictions, and future considerations are described in this README. The goal has been to produce a design that is both extendable and functional, allowing for future improvements as needed.
