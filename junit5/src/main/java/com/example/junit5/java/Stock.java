package com.example.junit5.java;

/***
 *
 * Created on：08/05/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class Stock {
    private String stockId;
    private String name;
    private int quantity;

    public Stock(String stockId, String name, int quantity) {
        this.stockId = stockId;
        this.name = name;
        this.quantity = quantity;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
