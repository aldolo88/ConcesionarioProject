package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
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
    @NotNull
    @Column(name = "serial_number")
    private String serialNumber;

    public Product() {
    }

    public Product(Vehicle vehicle, String serialNumber) {
        this.vehicle = vehicle;
        this.serialNumber = serialNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + this.id +
                ", vehicle=" + this.vehicle +
                ", serialNumber=" + this.serialNumber +
                '}';
    }
}
