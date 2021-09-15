package com.example.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="products")
public class Product {

    @Id @GeneratedValue private long id;
    @JoinColumn(name="vehicle_id") @ManyToOne private Vehicle vehicle;
    // @OneToMany(mappedBy = "product") private List<SaleDetail> saleProducts;
    @Column(name="serial_number") private double serialNumber;

    public Product(){}

}
