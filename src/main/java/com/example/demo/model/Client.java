package com.example.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="clients")
public class Client {

    @Id @GeneratedValue private long id;
    private String DNI;
    private String name;
    private int phone;
//    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL) private List<Sale> sales;

    public Client(){}
}
