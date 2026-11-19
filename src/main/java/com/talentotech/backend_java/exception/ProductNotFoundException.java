package com.talentotech.backend_java.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("The product with id " + id + " not found");
    }

    public ProductNotFoundException(String name) {
        super("The product with name " + name + " not found");
    }
}
