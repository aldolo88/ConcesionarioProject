package com.example.demo.model;

import javax.validation.constraints.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String dni;

    @NotNull
    private String name;
    private int phone;
//    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL) private List<Sale> sales;

    public Client() {
    }

    public Client(String dni, String name, int phone) {
        this.dni = dni;
        this.name = name;
        this.phone = phone;
    }

    public long getId() {return id;}

    public void setId(long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}
