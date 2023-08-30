package com.atelier.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @SequenceGenerator(name = "product_seq", sequenceName = "product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    private String name;
    private Float price;

    @ElementCollection
    private List<String> imagesPath;

    @ElementCollection
    private Set<String> availableColors;

    @ElementCollection
    private Set<String> availableSizes;

//    @ManyToMany(mappedBy = "products")
//    private List<UserOrder> orders;

    public Product(String name, Float price, List<String> imagesPath, Set<String> availableColors, Set<String> availableSizes) {
        this.name = name;
        this.price = price;
        this.imagesPath = imagesPath;
        this.availableColors = availableColors;
        this.availableSizes = availableSizes;
    }
}
