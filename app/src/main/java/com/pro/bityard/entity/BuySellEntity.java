package com.pro.bityard.entity;

public class BuySellEntity {

    private String price;
    private String amount;

    public BuySellEntity(String price, String amount) {
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BuySellEntity{" +
                "price=" + price +
                ", amount=" + amount +
                '}';
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
