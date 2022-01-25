package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sale_detail")
public class SaleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JoinColumn(name = "sale_id")
    @ManyToOne
    private Sale sale;

    @NotNull
    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @NotNull
    private double price;


    public SaleDetail() {
    }

    public SaleDetail(Sale sale, Product product, double price) {
        this.sale = sale;
        this.product = product;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "SaleDetail{" +
                "id=" + id +
                ", sale=" + sale +
                ", product=" + product +
                ", price=" + price +
                '}';
    }
}
