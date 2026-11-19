package com.talentotech.backend_java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;


@Entity
@Table(name = "products")
public class Product {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be null nor empty or just spaces")
    private String name;

    @NotNull(message = "Description cannot be null, but it can be empty")
    private String description;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany 
    @JoinTable(name = "products_suppliers", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "supplier_id"))
    private List<Supplier> suppliers;

    @OneToOne(mappedBy = "product")
    private Image image;
    
    @PositiveOrZero
    private int stock;

    @ManyToMany
    @JoinTable(name = "products_tags", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;

    protected Product() {}

    public Product(String name, String description, Long price, Category category, List<Supplier> suppliers, Image image, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.suppliers = suppliers;
        this.image = image;
        this.stock = stock;
        this.tags = tags;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Category getCategory() {
        return this.category;
    }

    public List<Supplier> getSuppliers() {
        return this.suppliers;
    }

    public Image getImage() {
        return this.image;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public List<Tag> getTags() { return this.tags; }
    
    public void setTags(List<Tag> tags) { this.tags = tags; }
}
