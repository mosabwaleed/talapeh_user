package com.example.talapehuser;

public class itmeList {
    String name;
    String price;
    int i;
    Double total,totaloftotal;
    String date;

    private itmeList(){}

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public itmeList(String name, String price, int i) {
        this.name = name;
        this.price = price;
        this.i=i;

    }

    public itmeList(String name, String price, int i, Double total) {
        this.name = name;
        this.price = price;
        this.i = i;
        this.total = total;
    }

    public itmeList(String name, String price, int i, String date) {
        this.name = name;
        this.price = price;
        this.i = i;
        this.date = date;
    }
    public itmeList(String name, int i, String date) {
        this.name = name;
        this.i = i;
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Double getTotal() {
        return total;
    }

    public String getDate() {
        return date;
    }
}
