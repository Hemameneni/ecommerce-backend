package com.ecommerce.ecommerce_backend.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private double totalAmount;
}

