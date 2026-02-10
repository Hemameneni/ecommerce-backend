package com.ecommerce.ecommerce_backend.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data

@AllArgsConstructor
public class CartItemResponse {
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private double totalPrice;
}

