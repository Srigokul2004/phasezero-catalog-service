package com.phasezero.catalog.model;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public class Product {
    private Long id;
    
    @NotBlank(message = "Part number is required")
    private String partNumber;
    
    @NotBlank(message = "Part name is required")
    private String partName;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    private double price;
    
    @Min(value = 0, message = "Stock cannot be negative")
    private int stock;
    
    private LocalDateTime createdAt;
    
    public Product() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Product(String partNumber, String partName, String category, double price, int stock) {
        this();
        this.partNumber = partNumber;
        this.partName = partName != null ? partName.toLowerCase() : null;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    
    public String getPartName() { return partName; }
    public void setPartName(String partName) { 
        this.partName = partName != null ? partName.toLowerCase() : null; 
    }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}