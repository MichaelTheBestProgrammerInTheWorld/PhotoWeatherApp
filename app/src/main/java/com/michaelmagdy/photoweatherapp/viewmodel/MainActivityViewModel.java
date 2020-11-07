package com.michaelmagdy.photoweatherapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.michaelmagdy.photoweatherapp.model.Repository;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;

public class MainActivityViewModel extends AndroidViewModel {

    private Repository repository;
    private MutableLiveData<WeatherPojo> weatherLiveData = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository();
    }

    public MutableLiveData<WeatherPojo> getWeatherLiveData() {
        weatherLiveData = repository.getCurrentWeather();
        return weatherLiveData;
    }
}
