package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JoinColumn(name = "client_id")
    @ManyToOne
    private Client client;

    @NotNull
    @JoinColumn(name = "employee_id")
    @ManyToOne
    private Employee employee;

    @NotNull
    @Column(name = "sale_date")
    private Date saleDate;
//    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL) private List<SaleDetail> salelines;

    public Sale() {
    }

    public Sale(Client client, Employee employee, Date saleDate) {
        this.client = client;
        this.employee = employee;
        this.saleDate = saleDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + this.id +
                ", client=" + this.client +
                ", employee=" + this.employee +
                ", saleDate=" + this.saleDate +
                '}';
    }
}
