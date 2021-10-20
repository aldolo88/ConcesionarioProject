package com.example.demo.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "provider_id")
    @ManyToOne
    private Provider provider;
    @JoinColumn(name = "employee_id")
    @ManyToOne
    private Employee employee;
    @Column(name = "order_date")
    private Date orderDate;
    //    @OneToMany(mappedBy = "order") private List<OrderDetail> orderDetails;

    public Order() {
    }

}
