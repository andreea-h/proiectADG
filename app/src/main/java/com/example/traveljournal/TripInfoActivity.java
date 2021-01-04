package com.example.traveljournal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.traveljournal.myFragments.HomeFragment;

import java.io.File;
import java.util.StringTokenizer;

public class TripInfoActivity extends AppCompatActivity {

    public static final String TRIP_ID = "tripID";
    public static final String TRIP_NAME = "name";
    public static final String TRIP_DESTINATION = "destination";
    public static final String TRIP_TYPE = "type";
    public static final String TRIP_PRICE = "price";
    public static final String TRIP_START_DATE = "start_date";
    public static final String TRIP_END_DATE = "end_date";
    public static final String TRIP_RATING = "rating";
    public static final String TRIP_PHOTO_PATH = "photo_path";

    //getExtra data (for edit trip action case)
    String tripId;
    String tripName;
    String tripDestination;
    String tripTypeAsString;
    float tripPrice;
    String startData;
    String endData;
    float tripRating;
    String photoPathString;
    boolean isFavourite;

    //screen elements
    public ImageView image;
    public TextView name;
    public TextView destination;
    public TextView price;
    public TextView startDate;
    public TextView endDate;
    public RatingBar rating;
    public TextView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_info);

        initScreenElements();

        //get trip infos
        tripId = getIntent().getStringExtra(HomeFragment.TRIP_ID);
        if (tripId != null) {
            tripName = getIntent().getStringExtra(HomeFragment.TRIP_NAME);
            tripDestination = getIntent().getStringExtra(HomeFragment.TRIP_DESTINATION);
            tripTypeAsString = getIntent().getStringExtra(HomeFragment.TRIP_TYPE);
            tripPrice = getIntent().getExtras().getFloat(HomeFragment.TRIP_PRICE);
            startData = getIntent().getStringExtra(HomeFragment.TRIP_START_DATE);
            endData = getIntent().getStringExtra(HomeFragment.TRIP_END_DATE);
            tripRating = getIntent().getExtras().getFloat(HomeFragment.TRIP_RATING);
            photoPathString = getIntent().getStringExtra(HomeFragment.TRIP_PHOTO_PATH);
            isFavourite = getIntent().getExtras().getBoolean(HomeFragment.TRIP_IS_FAV);
        }

        name.setText(tripName);
        destination.setText(tripDestination);

        type.setText(tripTypeAsString);

        rating.setRating(tripRating);

        File imageFile = new File(photoPathString);
        if(imageFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            image.setImageBitmap(myBitmap);
            image.setClipToOutline(true);
        }

        price.setText(" " + tripPrice + " euro");

        startDate.setText(startData);
        endDate.setText(endData);
    }

    public void initScreenElements() {
        image = findViewById(R.id.trip_image);
        name = findViewById(R.id.trip_name);
        destination = findViewById(R.id.trip_destination);
        type = findViewById(R.id.trip_type);
        rating = findViewById(R.id.rating);
        startDate = findViewById(R.id.trip_start_date);
        endDate = findViewById(R.id.trip_end_date);
        price = findViewById(R.id.trip_price);
    }
}