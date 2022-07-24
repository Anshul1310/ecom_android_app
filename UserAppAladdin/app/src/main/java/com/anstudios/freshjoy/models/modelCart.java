package com.anstudios.freshjoy.models;

import com.anstudios.freshjoy.Constant;

public class modelCart {
    String image;
    String quantity;
    String productId;
    String measuringUnit;
    String price;
    String title;
    String seller;
    String moq;

    public String getSeller() {
        return seller;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public modelCart(String measuringUnit, String seller, String productId, String moq, String image, String quantity, String price, String title) {
        this.image = image;
        this.measuringUnit=measuringUnit;
        this.moq = moq;
        this.seller=seller;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.title = title;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return  Constant.baseUrl.replace("api/","")+image.replaceFirst("/","");
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
