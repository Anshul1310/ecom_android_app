package com.anstudios.freshjoy.models;

import android.util.Log;

import com.anstudios.freshjoy.Constant;

public class modelhomeProducts {
    private String image, title;

    public modelhomeProducts(String image, String title) {
        this.image = image;
        this.title = title;
    }

    public String getImage() {
        Log.d("anshul",Constant.baseUrl.replace("api/","")+image.replaceFirst("/",""));
        return Constant.baseUrl.replace("api/","")+image.replaceFirst("/","");
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
}
