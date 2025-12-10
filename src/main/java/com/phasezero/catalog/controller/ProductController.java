package com.phasezero.catalog.controller;

import com.phasezero.catalog.dto.InventoryValueResponse;
import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.exception.ErrorResponse;
import com.phasezero.catalog.exception.ProductAlreadyExistsException;
import com.phasezero.catalog.model.Product;
import com.phasezero.catalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().get(0).getDefaultMessage();
            return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage, "VALIDATION_ERROR"));
        }
        
        try {
            Product product = productService.createProduct(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (ProductAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage(), "DUPLICATE_PART_NUMBER"));
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/sorted")
    public ResponseEntity<List<Product>> getProductsSortedByPrice() {
        List<Product> products = productService.getProductsSortedByPrice();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/inventory/value")
    public ResponseEntity<InventoryValueResponse> getTotalInventoryValue() {
        double totalValue = productService.calculateTotalInventoryValue();
        return ResponseEntity.ok(new InventoryValueResponse(totalValue));
    }
}