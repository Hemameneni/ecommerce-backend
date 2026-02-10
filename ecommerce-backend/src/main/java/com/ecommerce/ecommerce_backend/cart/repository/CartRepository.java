package com.ecommerce.ecommerce_backend.cart.repository;

import com.ecommerce.ecommerce_backend.cart.entity.Cart;
import com.ecommerce.ecommerce_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);


}

