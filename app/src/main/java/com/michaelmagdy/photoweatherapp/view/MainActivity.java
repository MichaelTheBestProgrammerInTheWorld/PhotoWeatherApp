package com.michaelmagdy.photoweatherapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.michaelmagdy.photoweatherapp.R;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;
import com.michaelmagdy.photoweatherapp.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textV);

        //mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainActivityViewModel.class);

        LiveData<WeatherPojo> weatherPojoLiveData = mainActivityViewModel.getWeatherLiveData();

        weatherPojoLiveData.observe(MainActivity.this, new Observer<WeatherPojo>() {
            @Override
            public void onChanged(WeatherPojo weatherPojo) {
                textView.setText(weatherPojo.getCityName() + "\n" +
                        weatherPojo.getTemp() + "\n" +
                        weatherPojo.getDescription());

                Log.d("weather_livedata", weatherPojo.getCityName() + "  "
                + weatherPojo.getTemp() + "  " + weatherPojo.getDescription());
            }
        } );
    }
}