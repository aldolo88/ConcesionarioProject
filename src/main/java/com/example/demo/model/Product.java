package com.example.demo.model;

import javax.persistence.*;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "provider_id", referencedColumnName = "provider_id"),
            @JoinColumn(name = "model", referencedColumnName = "model"),
            @JoinColumn(name = "colour", referencedColumnName = "colour"),
            @JoinColumn(name = "horsePower", referencedColumnName = "horsePower"),
            @JoinColumn(name = "type", referencedColumnName = "type"),
    })
    private Vehicle vehicle;
    // @OneToMany(mappedBy = "product") private List<SaleDetail> saleProducts;
    @Column(name = "serial_number")
    private double serialNumber;

    public Product() {
    }

}
