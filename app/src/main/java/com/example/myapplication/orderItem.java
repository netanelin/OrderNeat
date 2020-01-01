package com.example.myapplication;

class orderItem {
    private long orderId;
    private long productId;

    public orderItem() {
    }

    public orderItem(long orderId, long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
