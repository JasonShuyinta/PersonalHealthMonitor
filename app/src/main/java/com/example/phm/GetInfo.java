package com.example.phm;

//Class created to help make the charts
public class GetInfo {
    private String date;
    private int info;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getInformation() {
        return info;
    }

    public void setInformation(int info) {
        this.info = info;
    }

    public GetInfo(String date, int info) {
        this.date = date;
        this.info = info;
    }
}
