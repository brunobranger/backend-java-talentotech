package com.talentotech.entrega_final.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("The product with id " + id + " not found");
    }

    public ProductNotFoundException(String name) {
        super("The product with name " + name + " not found");
    }
}
