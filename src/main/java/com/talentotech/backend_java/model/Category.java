package com.talentotech.backend_java.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "categories")
public class Category {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank(message = "Category cannot be null nor empty. A product belongs to a category")
    String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    protected Category() {}

    public Category(Long id, String name, List<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Product> getProducts() {
        return this.products;
    }

}
