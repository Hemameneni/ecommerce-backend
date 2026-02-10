package com.ecommerce.ecommerce_backend.cart.dto;



import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddToCartRequest {
    private Long cartId;
    private Long productId;
    private int quantity;
}

