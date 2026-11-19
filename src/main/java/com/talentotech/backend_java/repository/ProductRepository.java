package com.talentotech.backend_java.repository;

import com.talentotech.backend_java.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);
    List<Product> findByNameContainingIgnoreCase(String text);
    List<Product> findByPriceGreaterThanEqual(Long price, Sort sort);
    List<Product> findByPriceLessThanEqual(Long price, Sort sort);
    List<Product> findByPriceBetween(Long min, Long max, Sort sort);
}
