package com.anstudios.freshjoy.models;

public class Items {
    String title;
    String image, store,price, slashedPrice, measuringUnit;

    public String getTitle() {
        return title;
    }

    public Items() {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(String slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }
}
