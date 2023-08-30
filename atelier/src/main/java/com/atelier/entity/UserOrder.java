package com.atelier.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_order")
public class UserOrder {

    @Id
    @SequenceGenerator(name = "order_seq", sequenceName = "order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    private Long id;

//    @ManyToMany
//    @JoinTable(
//        name = "order_product",
//        joinColumns = @JoinColumn(name = "order_id"),
//        inverseJoinColumns = @JoinColumn(name = "product_id")
//    )
//    private List<Product> products;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference
    private List<ProductOrder> productOrders;


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private AppUser user;

    private LocalDate date;
    private LocalTime time;
    private String status;

    public UserOrder(AppUser user, LocalDate date, LocalTime time, String status) {
        this.user = user;
        this.date = date;
        this.time = time;
        this.status = status;
    }
}
