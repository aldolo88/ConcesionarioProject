package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @JoinColumn(name = "client_id")
    @ManyToOne
    private Client client;
    @JoinColumn(name = "employee_id")
    @ManyToOne
    private Employee employee;
    @Column(name = "sale_date")
    private Date saleDate;
//    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL) private List<SaleDetail> salelines;

    public Sale() {
    }
}
