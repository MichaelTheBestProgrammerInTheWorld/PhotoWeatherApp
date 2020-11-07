package com.michaelmagdy.photoweatherapp.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.michaelmagdy.photoweatherapp.model.localdatabase.Picture;
import com.michaelmagdy.photoweatherapp.model.localdatabase.PictureDao;
import com.michaelmagdy.photoweatherapp.model.localdatabase.PictureDatabase;
import com.michaelmagdy.photoweatherapp.model.webservice.ApiClient;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherApi;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private MutableLiveData<WeatherPojo> weatherLiveData = new MutableLiveData<>();

    //getting picture history
    private PictureDao pictureDao;
    private LiveData<List<Picture>> allPics;

    public Repository(Application application) {
        PictureDatabase database = PictureDatabase.getInstance(application);
        pictureDao = database.pictureDao();
        allPics = pictureDao.getAllPictures();
    }

    //getting current weather details: temp, description and city name from
    //https://openweathermap.org/   API
    public MutableLiveData<WeatherPojo> getCurrentWeather(){

        String lat = "30.0626300";
        String longi = "31.2496700";

        ApiClient.getApiClient().getApiInterface().getWeather(lat, longi)
                .enqueue(new Callback<WeatherApi>() {
                    @Override
                    public void onResponse(Call<WeatherApi> call, Response<WeatherApi> response) {
                        WeatherApi apiResponse = response.body();
                        if (apiResponse != null){
                            String name = apiResponse.getName();
                            String temp = String.valueOf(apiResponse.getMain().getTemp());
                            String description = apiResponse.getWeather().get(0).getDescription();

                            WeatherPojo weatherPojo = new WeatherPojo();
                            weatherPojo.setCityName(name);
                            weatherPojo.setTemp(temp);
                            weatherPojo.setDescription(description);

                            weatherLiveData.setValue(weatherPojo);
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherApi> call, Throwable t) {

                        Log.d("WEATHER_EXCEPTION", t.getMessage());
                    }
                });

        return weatherLiveData;
    }


    //handling database operations

    public void insert(Picture picture) {
        //new InsertNoteAsyncTask(noteDao).execute(note);
        Executor myExecutor = Executors.newSingleThreadExecutor();
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                pictureDao.insert(picture);
            }
        });
    }

    public LiveData<List<Picture>> getAllPics() {
        return allPics;
    }
}
