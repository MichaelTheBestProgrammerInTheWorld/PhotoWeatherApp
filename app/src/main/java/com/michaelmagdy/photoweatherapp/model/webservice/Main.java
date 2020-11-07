package com.michaelmagdy.photoweatherapp.model.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {
    @Expose
    @SerializedName("humidity")
    private int humidity;
    @Expose
    @SerializedName("pressure")
    private int pressure;
    @Expose
    @SerializedName("temp_max")
    private double temp_max;
    @Expose
    @SerializedName("temp_min")
    private double temp_min;
    @Expose
    @SerializedName("feels_like")
    private double feels_like;
    @Expose
    @SerializedName("temp")
    private double temp;

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }
}
