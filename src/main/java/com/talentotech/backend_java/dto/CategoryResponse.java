package com.talentotech.backend_java.dto;

import com.talentotech.backend_java.model.Category;

public record CategoryResponse(Long id, String name) {

    public static CategoryResponse from(Category category) {
        return category == null ? null : new CategoryResponse(category.getId(), category.getName());
    }
}
