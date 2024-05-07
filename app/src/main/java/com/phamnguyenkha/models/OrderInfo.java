package com.phamnguyenkha.models;

import java.util.List;

public class OrderInfo {
    private String userId;
    private List<String> productIds;
    public  OrderInfo(){

    }
    public OrderInfo(String userId, List<String> productIds) {
        this.userId = userId;
        this.productIds = productIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }
}
