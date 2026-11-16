package com.talentotech.entrega_final.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequest(
        @NotBlank(message = "Name cannot be null nor empty or just spaces") String name,
        @NotNull(message = "Description cannot be null, but it can be empty") String description,
        @NotNull(message = "Price cannot be null") @Positive(message = "Price must be positive") Long price,
        @PositiveOrZero(message = "Stock cannot be negative") int stock,
        Long categoryId) {
}
