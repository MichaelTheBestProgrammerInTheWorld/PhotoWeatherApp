package com.michaelmagdy.photoweatherapp.model.webservice;

import java.io.Serializable;

public class WeatherPojo implements Serializable {

    String cityName;
    String temp;
    String description;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
