package com.technical.myapplication.ClientsViewStuff;

public class model_class {


    String status;
    int rating,W_ID,UID;
    String Phno;
    String FName;
    double lat,lng;
    public model_class(String FName , String Phno , int rating ,String status,int W_ID,int UID , double lat ,double lng) {
        this.Phno = Phno;
        this.FName = FName;
        this.rating = rating;
        this.status = status;
        this.W_ID = W_ID;
        this.UID = UID;
        this.lat = lat;
        this.lng = lng;

    }

    public String getStatus() {
        return status;
    }

    public int getUID() {
        return UID;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public int getRating() {
        return rating;
    }

    public int getW_ID() {
        return W_ID;
    }

    public String getPhno() {
        return Phno;
    }

    public String getFName() {
        return FName;
    }



}



