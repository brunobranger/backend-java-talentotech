package com.talentotech.entrega_final.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("The product with id " + id + " not found");
    }
}
