package com.anstudios.freshjoy.models;

public class modelOrdersPage {
    private String image, title,seller, measuringUnit, quantity, price;

    public modelOrdersPage(String image, String title, String quantity, String price,String measuringUnit,String seller) {
        this.image = image;
        this.title = title;
        this.measuringUnit=measuringUnit;
        this.seller=seller;
        this.quantity = quantity;
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

