package com.technical.myapplication.WorkerViewSuff;

public class WorkerModelClass {

   private int id;
   private String Phno;
   private String FName ,LName;
   private double lat,lng ;
   int rating;

    public WorkerModelClass( int id  , String FName, String LName ,String phno, double lat , double lng , int rating) {
        this.Phno = phno;
        this.FName = FName;
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.rating = rating;
        this.LName = LName;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getPhno() {
        return Phno;
    }

    public String getFName() {
        return FName;
    }

    public String getLName() {
        return LName;
    }
}






