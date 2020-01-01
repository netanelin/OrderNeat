package com.example.myapplication;

class Order {
    private long userAccountId;
    private long orderId;
    private long cardNumber;
    private double totalCost;
    private int tableNumber;
    private long timestamp;
    private boolean served;

    public Order() {
    }

    public Order(long userAccountId, long orderId, long cardNumber, double totalCost, int tableNumber, long timestamp, boolean served) {
        this.userAccountId = userAccountId;
        this.orderId = orderId;
        this.cardNumber = cardNumber;
        this.totalCost = totalCost;
        this.tableNumber = tableNumber;
        this.timestamp = timestamp;
        this.served = served;
    }

    public long getUserAccountId() {
        return userAccountId;
    }

    public void setUserAccountId(long userAccountId) {
        this.userAccountId = userAccountId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }
}
