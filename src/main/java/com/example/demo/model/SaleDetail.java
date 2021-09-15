package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name="sale_detail")
public class SaleDetail {

    @Id @GeneratedValue private long id;
    @JoinColumn(name="sale_id") @ManyToOne private Sale sale;
    @JoinColumn(name="product_id") @ManyToOne private Product product;
    private double price;


    public SaleDetail(){}
}
