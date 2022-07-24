package com.anstudios.sellerapp.models;

import org.json.JSONArray;

import java.util.HashMap;

public class modelOrders {
    String userId;
    String phoneNumber;
    String note;

    String totalPrice;
    String address;
    String paymentType;
    String orderId;
    String status;
    String dateTime;
    JSONArray items;
    HashMap<String, HashMap<String, String>> hashMap;

    public modelOrders() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public JSONArray getItems() {
        return items;
    }

    public void setItems(JSONArray items) {
        this.items = items;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public HashMap<String, HashMap<String, String>> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, HashMap<String, String>> hashMap) {
        this.hashMap = hashMap;
    }
}
