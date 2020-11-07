package com.michaelmagdy.photoweatherapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.michaelmagdy.photoweatherapp.model.Repository;
import com.michaelmagdy.photoweatherapp.model.localdatabase.Picture;

public class ImageFullScreenActivityViewModel extends AndroidViewModel {

    private Repository repository;

    public ImageFullScreenActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
    }

    public void insert(Picture picture) {
        repository.insert(picture);
    }
}
