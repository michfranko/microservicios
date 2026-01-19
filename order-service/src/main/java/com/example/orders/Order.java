package com.example.orders;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order extends PanacheEntity {
    public Long productId;
    public String destination;
    public int quantity;
    public double shippingCost;
    public double total;

    public Order() {
    }

    public Order(Long productId, String destination, int quantity, double shippingCost, double total) {
        this.productId = productId;
        this.destination = destination;
        this.quantity = quantity;
        this.shippingCost = shippingCost;
        this.total = total;
    }
}
