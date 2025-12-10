package com.phasezero.catalog.repository;

import com.phasezero.catalog.model.Product;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        products.put(product.getPartNumber(), product);
        return product;
    }
    
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }
    
    public Optional<Product> findByPartNumber(String partNumber) {
        return Optional.ofNullable(products.get(partNumber));
    }
    
    public boolean existsByPartNumber(String partNumber) {
        return products.containsKey(partNumber);
    }
    
    public List<Product> findByPartNameContainingIgnoreCase(String partName) {
        return products.values().stream()
                .filter(p -> p.getPartName().toLowerCase().contains(partName.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Product> findByCategory(String category) {
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    
    public List<Product> findAllOrderByPriceAsc() {
        return products.values().stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }
}