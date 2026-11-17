package com.talentotech.entrega_final.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity 
@Table(name = "suppliers")
public class Supplier {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank 
    String name;

    @ManyToMany(mappedBy = "suppliers")
    private List<Product> products;

    protected Supplier() {}

    public Supplier(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public List<Product> getProduct() {
        return this.products;   
    }
}
