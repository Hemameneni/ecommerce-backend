
package com.ecommerce.ecommerce_backend.product.controller;

import com.ecommerce.ecommerce_backend.product.dto.CreateProductRequest;
import com.ecommerce.ecommerce_backend.product.dto.ProductResponse;
import com.ecommerce.ecommerce_backend.product.dto.UpdateProductRequest;
import com.ecommerce.ecommerce_backend.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @RequestBody @Valid CreateProductRequest request
    ) {
        return productService.createProduct(request);
    }

    @GetMapping
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }
    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProductRequest request
    ) {
        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    @GetMapping("/test")
    public String test(){
        return "ok";
    }

}
