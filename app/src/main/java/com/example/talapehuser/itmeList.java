package com.example.talapehuser;

public class itmeList {
    String name;
    String price;
    int i=0;

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


}
