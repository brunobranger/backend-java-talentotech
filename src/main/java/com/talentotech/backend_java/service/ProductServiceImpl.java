package com.talentotech.backend_java.service;
import com.talentotech.backend_java.dto.CreateProductRequest;
import com.talentotech.backend_java.model.Category;
import com.talentotech.backend_java.model.Product;
import com.talentotech.backend_java.repository.CategoryRepository;
import com.talentotech.backend_java.repository.ProductRepository;
import org.springframework.stereotype.Service;
import com.talentotech.backend_java.exception.ProductNotFoundException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired 
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(CreateProductRequest request) {
        Category category = null;
        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Category with id " + request.categoryId() + " not found"));
        }

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                category,
                null,
                null,
                request.stock());
        return productRepository.save(product);
    }

    public List<Product> getProducts(Long minPrice, Long maxPrice, String sort) {
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice cannot be greater than maxPrice");
        }

        Sort sortOrder = sortOrder(sort);

        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice, sortOrder);
        }
        if (minPrice != null) {
            return productRepository.findByPriceGreaterThanEqual(minPrice, sortOrder);
        }
        if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqual(maxPrice, sortOrder);
        }
        return productRepository.findAll(sortOrder);
    }

    private Sort sortOrder(String sort) {
        if ("price_asc".equalsIgnoreCase(sort)) {
            return Sort.by("price").ascending();
        }
        if ("price_desc".equalsIgnoreCase(sort)) {
            return Sort.by("price").descending();
        }
        return Sort.unsorted();
    }

    public Product getProductByName(String name) {
        return productRepository.findByName(name).orElseThrow(() -> new ProductNotFoundException(name));
    }

    public List<Product> searchProducts(String text) {
        return productRepository.findByNameContainingIgnoreCase(text);
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
