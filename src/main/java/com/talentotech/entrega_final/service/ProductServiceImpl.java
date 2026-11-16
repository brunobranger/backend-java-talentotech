package com.talentotech.entrega_final.service;
import com.talentotech.entrega_final.model.Product;
import com.talentotech.entrega_final.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.talentotech.entrega_final.exception.ProductNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Autowired 
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductByName(String name) {
        return productRepository.getProductByName(name);
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Product updateProduct(Long productId, Long price, int stock) {
        Product product = getProductById(productId);
        product.setPrice(price);
        product.setStock(stock);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
