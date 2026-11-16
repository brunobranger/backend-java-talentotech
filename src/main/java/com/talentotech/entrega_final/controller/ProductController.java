package com.talentotech.entrega_final.controller;

import com.talentotech.entrega_final.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.talentotech.entrega_final.model.Product;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController 
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired 
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.OK);
    }

    @GetMapping 
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        Product product = productService.getProductByName(name);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Long price, int stock) {
        Product updatedProduct = productService.updateProduct(id, price, stock);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } 

}
