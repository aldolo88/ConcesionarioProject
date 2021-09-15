package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name="stock")
public class Stock {

    @Id @Column(name="vehicle_id") private long id;
    @OneToOne @MapsId @JoinColumn(name="vehicle_id") private Vehicle vehicle;
    private long amount;

    public Stock(){}
}
