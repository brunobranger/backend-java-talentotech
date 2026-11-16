package com.talentotech.entrega_final.dto;

import com.talentotech.entrega_final.model.Category;

public record CategoryResponse(Long id, String name) {

    public static CategoryResponse from(Category category) {
        return category == null ? null : new CategoryResponse(category.getId(), category.getName());
    }
}
