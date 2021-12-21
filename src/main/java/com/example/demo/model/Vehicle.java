package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @EmbeddedId
    private VehicleId id;
    @NotNull
    private double pvp;



    // @OneToMany(mappedBy = "vehicle") private List<Product> products;
    //@OneToOne(mappedBy = "vehicle")
    //@PrimaryKeyJoinColumn
    //private Stock stock;

    public Vehicle() {
    }

    public Vehicle(VehicleId id, double pvp) {
        this.id = id;
        this.pvp = pvp;
    }

    public VehicleId getId() {
        return id;
    }

    public void setVehicleId(VehicleId id) {
        this.id = id;
    }

    public double getPvp() {
        return pvp;
    }

    public void setPvp(double pvp) {
        this.pvp = pvp;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + this.id +
                ", pvp=" + this.pvp +
                '}';
    }
}