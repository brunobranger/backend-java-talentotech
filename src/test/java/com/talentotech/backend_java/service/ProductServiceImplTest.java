package com.talentotech.backend_java.service;

import com.talentotech.backend_java.exception.ProductNotFoundException;
import com.talentotech.backend_java.dto.CreateProductRequest;
import com.talentotech.backend_java.model.Category;
import com.talentotech.backend_java.model.Product;
import com.talentotech.backend_java.repository.CategoryRepository;
import com.talentotech.backend_java.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductsReturnsAllWithoutFiltersNorSort() {
        List<Product> products = List.of();
        when(productRepository.findAll(Sort.unsorted())).thenReturn(products);

        assertEquals(products, productService.getProducts(null, null, null));
        verify(productRepository).findAll(Sort.unsorted());
    }

    @Test
    void getProductsSortsByPriceAscending() {
        Sort sort = Sort.by("price").ascending();
        when(productRepository.findAll(sort)).thenReturn(List.of());

        productService.getProducts(null, null, "price_asc");

        verify(productRepository).findAll(sort);
    }

    @Test
    void getProductsSortsByPriceDescending() {
        Sort sort = Sort.by("price").descending();
        when(productRepository.findAll(sort)).thenReturn(List.of());

        productService.getProducts(null, null, "price_desc");

        verify(productRepository).findAll(sort);
    }

    @Test
    void getProductsFiltersByMinPrice() {
        Sort sort = Sort.by("price").ascending();
        when(productRepository.findByPriceGreaterThanEqual(100L, sort)).thenReturn(List.of());

        productService.getProducts(100L, null, "price_asc");

        verify(productRepository).findByPriceGreaterThanEqual(100L, sort);
    }

    @Test
    void getProductsFiltersByMaxPrice() {
        when(productRepository.findByPriceLessThanEqual(500L, Sort.unsorted())).thenReturn(List.of());

        productService.getProducts(null, 500L, null);

        verify(productRepository).findByPriceLessThanEqual(500L, Sort.unsorted());
    }

    @Test
    void getProductsFiltersByPriceRange() {
        Sort sort = Sort.by("price").ascending();
        when(productRepository.findByPriceBetween(100L, 500L, sort)).thenReturn(List.of());

        productService.getProducts(100L, 500L, "price_asc");

        verify(productRepository).findByPriceBetween(100L, 500L, sort);
    }

    @Test
    void getProductsRejectsInvalidPriceRange() {
        assertThrows(IllegalArgumentException.class,
                () -> productService.getProducts(500L, 100L, null));
    }

    @Test
    void getProductByIdThrowsWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    void searchProductsDelegatesToRepository() {
        List<Product> products = List.of();
        when(productRepository.findByNameContainingIgnoreCase("cami")).thenReturn(products);

        assertEquals(products, productService.searchProducts("cami"));
    }

    @Test
    void createProductResolvesCategoryAndSaves() {
        Category category = new Category(1L, "Camisas", null);
        CreateProductRequest request = new CreateProductRequest("Camisa", "Algodón", 15000L, 10, 1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product product = productService.createProduct(request);

        assertEquals("Camisa", product.getName());
        assertEquals(15000L, product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals(category, product.getCategory());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProductWithoutCategorySaves() {
        CreateProductRequest request = new CreateProductRequest("Camisa", "Algodón", 15000L, 10, null);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product product = productService.createProduct(request);

        assertNull(product.getCategory());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void createProductRejectsUnknownCategory() {
        CreateProductRequest request = new CreateProductRequest("Camisa", "Algodón", 15000L, 10, 99L);
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(request));
        verify(productRepository, never()).save(any(Product.class));
    }
}
