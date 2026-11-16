package com.talentotech.entrega_final.dto;

import com.talentotech.entrega_final.model.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Long price,
        int stock,
        CategoryResponse category) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                CategoryResponse.from(product.getCategory()));
    }
}
