package com.talentotech.entrega_final.repository;

import com.talentotech.entrega_final.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getProductByName(String name);
}
