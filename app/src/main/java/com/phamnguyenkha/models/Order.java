package com.phamnguyenkha.models;

import java.util.Date;
import java.util.List;

public class Order {
    private String userId;
    private String recipientName;
    private double totalPrice;
    private List<Product> products;
    private Date orderDate;
    private String paymentMethod;
    public Order(){

    }
    public Order(String userId, String recipientName, double totalPrice, List<Product> products, Date orderDate,String paymentMethod) {
        this.userId = userId;
        this.recipientName = recipientName;
        this.totalPrice = totalPrice;
        this.products = products;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
