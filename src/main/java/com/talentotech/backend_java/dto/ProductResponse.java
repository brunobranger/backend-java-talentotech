package com.talentotech.backend_java.dto;

import com.talentotech.backend_java.model.Product;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Long price,
        int stock,
        CategoryResponse category,
        String imageUrl) {

    public static ProductResponse from(Product product) {
        String imageUrl = product.getImage() != null ? product.getImage().getUrl() : null;
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                CategoryResponse.from(product.getCategory()),
                imageUrl);
    }
}
