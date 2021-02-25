package com.example.babyneeds.model;

public class Details {
    private int id;
    private String item;
    private int size;
    private String color;
    private int qty;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String date;
    public Details()
    {

    }
    public Details( String item, int qty, String color,int size) {

        this.item = item;
        this.size = size;
        this.color = color;
        this.qty = qty;
        this.date=String.valueOf(java.lang.System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
