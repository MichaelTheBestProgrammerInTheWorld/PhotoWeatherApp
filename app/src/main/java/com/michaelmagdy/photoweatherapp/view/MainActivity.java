package com.michaelmagdy.photoweatherapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.michaelmagdy.photoweatherapp.R;
import com.michaelmagdy.photoweatherapp.model.localdatabase.Picture;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;
import com.michaelmagdy.photoweatherapp.viewmodel.MainActivityViewModel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;

    FloatingActionButton mAddFab, mCameraFab, mGalleryFab;
    TextView cameraText, galleryText;
    Boolean isAllFabsVisible;

    private RecyclerView recyclerView;
    private PictureListAdapter adapter;

    private WeatherPojo currentWeatherPojo;
    public static final String INTENT_TYPE = "type";
    public static final String INTENT_WEATHER = "weather_pojo";
    public static final String INTENT_CAMERA = "camera";
    public static final String INTENT_GALLERY = "gallery";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addAppPermissions();
        printKeyHash(MainActivity.this);
        printHashKey();

        recyclerView = findViewById(R.id.picture_history_list);

        //mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainActivityViewModel.class);

        LiveData<WeatherPojo> weatherPojoLiveData = mainActivityViewModel.getWeatherLiveData();

        weatherPojoLiveData.observe(MainActivity.this, new Observer<WeatherPojo>() {
            @Override
            public void onChanged(WeatherPojo weatherPojo) {

                currentWeatherPojo = weatherPojo;

                Log.d("weather_livedata", weatherPojo.getCityName() + "  "
                + weatherPojo.getTemp() + "  " + weatherPojo.getDescription());
            }
        } );

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new PictureListAdapter();
        recyclerView.setAdapter(adapter);

        mainActivityViewModel.getAllPictures().observe(MainActivity.this, new Observer<List<Picture>>() {
            @Override
            public void onChanged(List<Picture> pictures) {

                adapter.submitList(pictures);
            }
        });

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
                        if (addAppPermissions()){
                            goToImageFullScreenActivity(INTENT_GALLERY);
                        } else {

                            Toast.makeText(MainActivity.this, "all permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        mCameraFab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (addAppPermissions()){
                            goToImageFullScreenActivity(INTENT_CAMERA);
                        } else {

                            Toast.makeText(MainActivity.this, "all permissions are required", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public boolean addAppPermissions(){

        final boolean[] isPermissionGranted = new boolean[1];

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()){
                            isPermissionGranted[0] = true;
                        } else {
                            Toast.makeText(MainActivity.this, "all permissions are required", Toast.LENGTH_SHORT).show();
                            isPermissionGranted[0] = false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                }).onSameThread()
                  .check();

        return isPermissionGranted[0];
    }

    private void goToImageFullScreenActivity(String type){

        Intent intent = new Intent(this, ImageFullScreenActivity.class);
        intent.putExtra(INTENT_WEATHER, currentWeatherPojo);
        intent.putExtra(INTENT_TYPE, type);
        startActivity(intent);
    }



    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("a7aB2a", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.michaelmagdy.photoweatherapp",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("a7aB2a:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }
}