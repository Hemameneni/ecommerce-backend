package com.ecommerce.ecommerce_backend.cart.service;

import com.ecommerce.ecommerce_backend.cart.dto.AddToCartRequest;
import com.ecommerce.ecommerce_backend.cart.dto.CartItemResponse;
import com.ecommerce.ecommerce_backend.cart.dto.CartResponse;
import com.ecommerce.ecommerce_backend.cart.entity.Cart;
import com.ecommerce.ecommerce_backend.cart.entity.CartItem;
import com.ecommerce.ecommerce_backend.cart.repository.CartItemRepository;
import com.ecommerce.ecommerce_backend.cart.repository.CartRepository;
import com.ecommerce.ecommerce_backend.exception.InsufficientStockException;
import com.ecommerce.ecommerce_backend.product.entity.Product;
import com.ecommerce.ecommerce_backend.product.repository.ProductRepository;
import com.ecommerce.ecommerce_backend.user.entity.User;
import com.ecommerce.ecommerce_backend.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    public Cart addToCart(Long cartId, Long productId, int quantity) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int availableStock = product.getStock();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem cartItem =
                cartItemRepository.findByCartIdAndProductId(cartId, productId);

        int alreadyInCart = (cartItem != null) ? cartItem.getQuantity() : 0;
        int totalRequested = alreadyInCart + quantity;

        // 🚨 STOCK CHECK
        if (totalRequested > availableStock) {
            throw new RuntimeException(
                    "Only " + availableStock + " items available in stock"
            );
        }

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(totalRequested);
        }

        cartItem.setPrice(product.getPrice());
        cartItemRepository.save(cartItem);

        return cart;
    }



    public CartResponse viewCart(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // 🔥 FIX: create cart if not exists
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });



        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),              // ✅ productId
                        item.getProduct().getName(),            // ✅ productName
                        item.getPrice(),                        // ✅ price
                        item.getQuantity(),                     // ✅ quantity
                        item.getPrice() * item.getQuantity()    // ✅ totalPrice
                ))
                .collect(Collectors.toList());

        double total = items.stream()
                .mapToDouble(CartItemResponse::getTotalPrice)
                .sum();

        return new CartResponse(items, total);
    }
    @Transactional
    public void updateItemQuantity(
            Long productId,
            int quantity,
            Authentication authentication
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (quantity > product.getStock()) {
            throw new InsufficientStockException(
                    "Only " + product.getStock() + " items available in stock"
            );
        }

        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem); // auto remove
        } else {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }
    @Transactional
    public void removeItem(
            Long productId,
            Authentication authentication
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));



        CartItem cartItem = cartItemRepository
                .findByCartAndProduct(cart, product)
                .orElseThrow(() -> new RuntimeException("Item not in cart"));

        cartItemRepository.delete(cartItem);
    }
    @Transactional
    public void clearCart(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteAll(cart.getItems());
    }



}

