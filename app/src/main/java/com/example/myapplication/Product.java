package com.example.myapplication;

class Product {

    private long id;
    private String name;
    private double price;
    private boolean available;

    public Product(){}
    public Product(long id, String name, double price, boolean available){
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
