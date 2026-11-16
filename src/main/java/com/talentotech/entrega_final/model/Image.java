package com.talentotech.entrega_final.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "images")
public class Image {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @OneToOne 
    @JoinColumn(name = "product_id")
    private Product product;

    public Image(Long id, String url, Product product) {
        this.id = id;
        this.url = url;
        this.product = product;
    }

    public Long getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public Product getProduct() {
        return this.product;
    }
    
    
}
