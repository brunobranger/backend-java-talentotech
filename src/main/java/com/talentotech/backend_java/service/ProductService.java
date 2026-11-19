package com.talentotech.backend_java.service;

import java.util.List;

import com.talentotech.backend_java.dto.CreateProductRequest;
import com.talentotech.backend_java.model.Product;

public interface ProductService {
    Product createProduct(CreateProductRequest request);
    List<Product> getProducts(Long minPrice, Long maxPrice, String sort);
    Product getProductByName(String name);
    List<Product> searchProducts(String text);
    Product getProductById(Long id);
    Product updateProduct(Long id, Long price, int stock);
    void deleteProduct(Long id);
}
