package com.talentotech.backend_java.controller;

import com.talentotech.backend_java.service.ProductService;
import com.talentotech.backend_java.dto.CreateProductRequest;
import com.talentotech.backend_java.dto.ProductResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired 
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponse createdProduct = ProductResponse.from(productService.createProduct(request));
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

    @GetMapping 
    public List<ProductResponse> getProducts(
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @RequestParam(required = false) String sort) {
        return productService.getProducts(minPrice, maxPrice, sort)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = ProductResponse.from(productService.getProductById(id));
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<ProductResponse> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestParam Long price, @RequestParam int stock) {
        ProductResponse updatedProduct = ProductResponse.from(productService.updateProduct(id, price, stock));
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 

}
