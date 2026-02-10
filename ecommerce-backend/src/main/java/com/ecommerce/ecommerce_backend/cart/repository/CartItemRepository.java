package com.ecommerce.ecommerce_backend.cart.repository;


import com.ecommerce.ecommerce_backend.cart.entity.Cart;
import com.ecommerce.ecommerce_backend.cart.entity.CartItem;
import com.ecommerce.ecommerce_backend.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndProductId(Long cartId, Long productId);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}


