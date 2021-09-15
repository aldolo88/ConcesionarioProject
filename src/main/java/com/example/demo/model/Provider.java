package com.example.demo.model;

import javax.persistence.*;

@Entity
@Table(name="providers")
public class Provider {

    @Id @GeneratedValue private long id;
    @Column(name="provider_name") String providerName;
    //    @OneToMany(mappedBy = "provider") private List<Order> orders;
    //    @OneToMany(mappedBy = "provider") private List<Vehicle> vehicles;

    public Provider() {}

}
