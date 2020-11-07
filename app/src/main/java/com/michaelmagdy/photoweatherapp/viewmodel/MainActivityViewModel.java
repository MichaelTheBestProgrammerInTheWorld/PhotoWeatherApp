package com.michaelmagdy.photoweatherapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.michaelmagdy.photoweatherapp.model.Repository;
import com.michaelmagdy.photoweatherapp.model.localdatabase.Picture;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private Repository repository;
    private MutableLiveData<WeatherPojo> weatherLiveData = new MutableLiveData<>();

    private LiveData<List<Picture>> allPictures;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        allPictures = repository.getAllPics();
    }

    public MutableLiveData<WeatherPojo> getWeatherLiveData() {
        weatherLiveData = repository.getCurrentWeather();
        return weatherLiveData;
    }


    public LiveData<List<Picture>> getAllPictures() {
        return allPictures;
    }
}
