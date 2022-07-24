package com.anstudios.sellerapp.models;

import com.anstudios.sellerapp.Constant;

public class News {
    String title;
    String description;
    String image;

    public News() {

    }

    public News(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return Constant.baseUrl.replace("api/","")+image.replaceFirst("/","");
    }

    public void setImage(String image) {
        this.image = image;
    }
}
