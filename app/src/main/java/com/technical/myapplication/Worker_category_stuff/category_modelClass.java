package com.technical.myapplication.Worker_category_stuff;

import java.io.Serializable;

public class category_modelClass implements Serializable {

private String name;
private int image;

    public category_modelClass() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
