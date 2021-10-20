package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;
    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;
    private double price;

    public OrderDetail() {
    }
}
