package com.michaelmagdy.photoweatherapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.michaelmagdy.photoweatherapp.R;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;
import com.michaelmagdy.photoweatherapp.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;

    FloatingActionButton mAddFab, mCameraFab, mGalleryFab;
    TextView cameraText, galleryText;
    Boolean isAllFabsVisible;

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


        mAddFab = findViewById(R.id.add_fab);
        mCameraFab = findViewById(R.id.camera_fab);
        mGalleryFab = findViewById(R.id.gallery_fab);
        cameraText = findViewById(R.id.camera_text);
        galleryText = findViewById(R.id.gallery_text);

        mCameraFab.setVisibility(View.GONE);
        mGalleryFab.setVisibility(View.GONE);
        cameraText.setVisibility(View.GONE);
        galleryText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAddFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            mCameraFab.show();
                            mGalleryFab.show();
                            cameraText.setVisibility(View.VISIBLE);
                            galleryText.setVisibility(View.VISIBLE);

                            isAllFabsVisible = true;
                        } else {

                            mCameraFab.hide();
                            mGalleryFab.hide();
                            cameraText.setVisibility(View.GONE);
                            galleryText.setVisibility(View.GONE);

                            isAllFabsVisible = false;
                        }
                    }
                });


        mGalleryFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Gallery Added", Toast.LENGTH_SHORT).show();
                    }
                });

        mCameraFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Camera Added", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}