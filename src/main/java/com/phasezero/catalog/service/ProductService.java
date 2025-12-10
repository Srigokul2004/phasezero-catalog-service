package com.phasezero.catalog.service;

import com.phasezero.catalog.dto.ProductRequest;
import com.phasezero.catalog.exception.ProductAlreadyExistsException;
import com.phasezero.catalog.model.Product;
import com.phasezero.catalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product createProduct(ProductRequest request) {
        if (productRepository.existsByPartNumber(request.getPartNumber())) {
            throw new ProductAlreadyExistsException("Product with part number " + request.getPartNumber() + " already exists");
        }
        
        Product product = new Product(
            request.getPartNumber(),
            request.getPartName(),
            request.getCategory(),
            request.getPrice(),
            request.getStock()
        );
        
        return productRepository.save(product);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByPartNameContainingIgnoreCase(name);
    }
    
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }
    
    public List<Product> getProductsSortedByPrice() {
        return productRepository.findAllOrderByPriceAsc();
    }
    
    public double calculateTotalInventoryValue() {
        return productRepository.findAll().stream()
                .mapToDouble(product -> product.getPrice() * product.getStock())
                .sum();
    }
}