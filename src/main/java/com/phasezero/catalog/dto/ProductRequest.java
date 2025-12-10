package com.phasezero.catalog.dto;

import jakarta.validation.constraints.*;

public class ProductRequest {
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
    
    // Getters and Setters
    public String getPartNumber() { return partNumber; }
    public void setPartNumber(String partNumber) { this.partNumber = partNumber; }
    
    public String getPartName() { return partName; }
    public void setPartName(String partName) { this.partName = partName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}