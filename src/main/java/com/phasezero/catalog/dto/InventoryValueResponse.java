package com.phasezero.catalog.dto;

public class InventoryValueResponse {
    private double totalValue;
    
    public InventoryValueResponse(double totalValue) {
        this.totalValue = totalValue;
    }
    
    public double getTotalValue() { return totalValue; }
    public void setTotalValue(double totalValue) { this.totalValue = totalValue; }
}