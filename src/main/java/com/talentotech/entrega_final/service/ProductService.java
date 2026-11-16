package com.talentotech.entrega_final.service;

import java.util.List;

import com.talentotech.entrega_final.dto.CreateProductRequest;
import com.talentotech.entrega_final.model.Product;

public interface ProductService {
    Product createProduct(CreateProductRequest request);
    List<Product> getProducts(Long minPrice, Long maxPrice, String sort);
    Product getProductByName(String name);
    List<Product> searchProducts(String text);
    Product getProductById(Long id);
    Product updateProduct(Long id, Long price, int stock);
    void deleteProduct(Long id);
}
