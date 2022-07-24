package com.anstudios.freshjoy.models;

import com.anstudios.freshjoy.Constant;

public class modelPriceList {
    String title, location, measuringUnit, price, image;

    public modelPriceList(String title, String location, String measuringUnit, String price, String image) {
        this.title = title;
        this.location = location;
        this.measuringUnit = measuringUnit;
        this.price = price;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMeasuringUnit() {
        return measuringUnit;
    }

    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return  Constant.baseUrl.replace("api/","")+image.replaceFirst("/","");
    }

    public void setImage(String image) {
        this.image = image;
    }
}
