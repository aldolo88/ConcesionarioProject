package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "providers")
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "provider_name")
    private String providerName;
    //    @OneToMany(mappedBy = "provider") private List<Order> orders;
    //    @OneToMany(mappedBy = "provider") private List<Vehicle> vehicles;

    public Provider() {
    }

    public Provider(String providerName) {
        this.providerName = providerName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
