package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @JoinColumn(name = "order_id")
    @ManyToOne
    private Order order;

    @NotNull
    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @NotNull
    private double price;

    public OrderDetail() {
    }

    public OrderDetail(Order order, Product product, double price) {
        this.order = order;
        this.product = product;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
        return "OrderDetails{" +
                "id=" + this.id +
                ", order=" + this.order +
                ", product=" + this.product +
                ", price=" + this.price +
                '}';
    }
}
