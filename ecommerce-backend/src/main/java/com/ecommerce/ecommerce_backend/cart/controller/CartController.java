package com.ecommerce.ecommerce_backend.cart.controller;

import com.ecommerce.ecommerce_backend.cart.dto.AddToCartRequest;
import com.ecommerce.ecommerce_backend.cart.dto.CartResponse;
import com.ecommerce.ecommerce_backend.cart.entity.Cart;
import com.ecommerce.ecommerce_backend.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;



    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request) {
        try {
            return ResponseEntity.ok(
                    cartService.addToCart(
                           request.getCartId(),
                            request.getProductId(),
                            request.getQuantity()
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<CartResponse> viewCart(Authentication authentication) {
        return ResponseEntity.ok(
                cartService.viewCart(authentication)
        );
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateItem(
            @RequestParam Long productId,
            @RequestParam int quantity,
            Authentication authentication
    ) {
        cartService.updateItemQuantity(productId, quantity, authentication);
        return ResponseEntity.ok("Cart updated");
    }
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long productId,
            Authentication authentication
    ) {
        cartService.removeItem(productId, authentication);
        return ResponseEntity.ok("Item removed from cart");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        cartService.clearCart(authentication);
        return ResponseEntity.ok("Cart cleared");
    }



}

