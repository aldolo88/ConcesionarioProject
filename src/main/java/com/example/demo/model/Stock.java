package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "stock")
public class Stock implements Serializable {

    @Id
    @OneToOne
    @MapsId
    @JoinColumns({
            @JoinColumn(name = "provider_id", referencedColumnName = "provider_id"),
            @JoinColumn(name = "model", referencedColumnName = "model"),
            @JoinColumn(name = "colour", referencedColumnName = "colour"),
            @JoinColumn(name = "horsePower", referencedColumnName = "horsePower"),
            @JoinColumn(name = "type", referencedColumnName = "type"),
    })

    private Vehicle vehicle;
    private long amount;


    public Stock() {
    }
}
