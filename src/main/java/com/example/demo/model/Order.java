package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JoinColumn(name = "provider_id")
    @ManyToOne
    private Provider provider;

    @NotNull
    @JoinColumn(name = "employee_id")
    @ManyToOne
    private Employee employee;

    @NotNull
    @Column(name = "order_date")
    private Date orderDate;
    //    @OneToMany(mappedBy = "order") private List<OrderDetail> orderDetails;

    public Order() {
    }

    public Order(Provider provider, Employee employee, Date orderDate) {
        this.provider = provider;
        this.employee = employee;
        this.orderDate = orderDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + this.id +
                ", provider=" + this.provider +
                ", employee=" + this.employee +
                ", orderDate=" + this.orderDate +
                '}';
    }
}
