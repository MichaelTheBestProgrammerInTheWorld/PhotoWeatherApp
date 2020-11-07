package com.michaelmagdy.photoweatherapp.model.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.michaelmagdy.photoweatherapp.model.webservice.Main;
import com.michaelmagdy.photoweatherapp.model.webservice.Weather;

import java.util.List;

public class WeatherApi {


    @Expose
    @SerializedName("cod")
    private int cod;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("timezone")
    private int timezone;
    @Expose
    @SerializedName("dt")
    private int dt;
    @Expose
    @SerializedName("main")
    private Main main;
    @Expose
    @SerializedName("base")
    private String base;
    @Expose
    @SerializedName("weather")
    private List<Weather> weather;


    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }
}
