package com.talentotech.entrega_final.service;

import java.util.List;

import com.talentotech.entrega_final.model.Product;

public interface ProductService {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getProductByName(String name);
    Product getProductById(Long id);
    Product updateProduct(Long id, Long price, int stock);
    void deleteProduct(Long id);
}
