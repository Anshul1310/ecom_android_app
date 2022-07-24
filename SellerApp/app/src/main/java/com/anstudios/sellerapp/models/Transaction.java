package com.anstudios.sellerapp.models;

public class Transaction {
    String payout, type, createdAt, seller;

    public Transaction(String payout, String type, String createdAt) {
        this.payout = payout;
        this.type = type;
        this.createdAt = createdAt;
    }
    public Transaction(){}


    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getPayout() {
        return payout;
    }

    public void setPayout(String payout) {
        this.payout = payout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
