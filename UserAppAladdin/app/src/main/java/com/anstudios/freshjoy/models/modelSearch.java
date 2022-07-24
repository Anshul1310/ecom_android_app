package com.anstudios.freshjoy.models;

import android.util.Log;

import com.anstudios.freshjoy.Constant;

public class modelSearch {
    String title;
    String measuringUnit;
    String moq;
    String slashedPrice;
    String productId;
    String seller;
    String image;
    String price;

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public modelSearch() {

    }

    public modelSearch(String seller,String productId, String title, String measuringUnit, String moq, String slashedPrice, String image, String price) {
        this.title = title;
        this.productId = productId;
        this.measuringUnit = measuringUnit;
        this.moq = moq;
        this.seller=seller;
        this.slashedPrice = slashedPrice;
        this.image = image;
        this.price = price;
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getMoq() {
        return moq;
    }

    public void setMoq(String moq) {
        this.moq = moq;
    }

    public String getSlashedPrice() {
        return slashedPrice;
    }

    public void setSlashedPrice(String slashedPrice) {
        this.slashedPrice = slashedPrice;
    }

    public String getImage() {
        Log.d("anshul",Constant.baseUrl.replace("api/","")+image.replaceFirst("/",""));

        return Constant.baseUrl.replace("api/","")+image.replaceFirst("/","");
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
