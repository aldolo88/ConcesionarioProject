package com.example.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="vehicles")
public class Vehicle {

    @Id @GeneratedValue private long id;
    @JoinColumn(name="provider_id") @ManyToOne private Provider provider;
    private String model;
    private String colour;
    @Column(name="horse_power") private int horsePower;
    private Type type;
    private double PVP;
    // @OneToMany(mappedBy = "vehicle") private List<Product> products;
    @OneToOne(mappedBy="vehicle") @PrimaryKeyJoinColumn private Stock stock;

    public Vehicle() {}

}

enum Type{COCHE,MOTO}