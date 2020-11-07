package com.michaelmagdy.photoweatherapp.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.palette.graphics.Palette;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.michaelmagdy.photoweatherapp.R;
import com.michaelmagdy.photoweatherapp.model.localdatabase.Picture;
import com.michaelmagdy.photoweatherapp.model.webservice.WeatherPojo;
import com.michaelmagdy.photoweatherapp.viewmodel.ImageFullScreenActivityViewModel;
import com.michaelmagdy.photoweatherapp.viewmodel.MainActivityViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.michaelmagdy.photoweatherapp.view.MainActivity.INTENT_CAMERA;
import static com.michaelmagdy.photoweatherapp.view.MainActivity.INTENT_GALLERY;
import static com.michaelmagdy.photoweatherapp.view.MainActivity.INTENT_TYPE;
import static com.michaelmagdy.photoweatherapp.view.MainActivity.INTENT_WEATHER;

public class ImageFullScreenActivity extends AppCompatActivity {

    private WeatherPojo weatherPojo;
    public static final int CAMERA_REQ_CODE = 5;
    public static final int GALLERY_REQ_CODE = 10;
    private Uri imgUri;
    private ImageView imageView;
    private Bitmap bitmap;

    private ShareButton photoShareBtn;
    private CallbackManager callbackManager;

    private ImageFullScreenActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);

        imageView = findViewById(R.id.image_full_screen);
        photoShareBtn = findViewById(R.id.sb_photo);
        callbackManager = CallbackManager.Factory.create();

        viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(ImageFullScreenActivityViewModel.class);

        Intent intent = getIntent();
        if (intent.hasExtra(INTENT_WEATHER)){
            weatherPojo = (WeatherPojo) intent.getSerializableExtra(INTENT_WEATHER);
        }
        if (intent.hasExtra(INTENT_TYPE)){

            if (intent.getStringExtra(INTENT_TYPE).equals(INTENT_CAMERA)){

                takePictureFromCamera();

            } else if (intent.getStringExtra(INTENT_TYPE).equals(INTENT_GALLERY)){

                selectImageFromGallery();

            }
        }
    }

    //handling camera onclick
    public void takePictureFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, CAMERA_REQ_CODE);
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }

    //handling gallery onclick
    public void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQ_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .build();

        photoShareBtn.setShareContent(sharePhotoContent);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE){
                imgUri = data.getData();
                bitmap = editImage(imgUri);
                imageView.setImageBitmap(bitmap);
            } else if (requestCode == CAMERA_REQ_CODE){

                bitmap = editImage(imgUri);
                imageView.setImageBitmap(bitmap);
            }
        }


        if (bitmap != null){
            com.michaelmagdy.photoweatherapp.model.localdatabase.Picture picture = new Picture(convertBitmapToByte(bitmap));
            viewModel.insert(picture);
        }
    }


    //draw weather details on picture
    private Bitmap editImage(Uri imageUri) {

        Bitmap result = null;

        try {
            Bitmap src = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(src, 0, 0, null);

            Paint paint = new Paint();
            int dominantColor = getColor(result);
            int dominantColorReversed = reverseColor(dominantColor);
            paint.setColor(dominantColorReversed);
            paint.setAlpha(255);
            paint.setTextSize(45);
            paint.setAntiAlias(true);
            paint.setUnderlineText(false);
            canvas.drawText(weatherPojo.getCityName(), 50, 50, paint);
            canvas.drawText(weatherPojo.getTemp() + "  F  " + weatherPojo.getDescription(), 50, 150, paint);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private int getColor(Bitmap bitmap) {
        return Palette.from(bitmap).generate().getDominantColor(Color.parseColor("#00FFFF"));
    }

    private int reverseColor(int color) {
        int a = Color.alpha(color);
        int r = 255 - Color.red(color);
        int g = 255 - Color.green(color);
        int b = 255 - Color.blue(color);
        return Color.argb(a,
                Math.max(r, 0),
                Math.max(g, 0),
                Math.max(b, 0));
    }

    //convert bitmap to byte[] to save it in database
    private byte[] convertBitmapToByte(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

}