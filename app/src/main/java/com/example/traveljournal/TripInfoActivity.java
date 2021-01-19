package com.example.traveljournal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.traveljournal.myFragments.HomeFragment;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.app.PendingIntent.getActivity;

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

    public TextView cityField;
    public TextView updatedField;
    public TextView detailsField;
    public TextView currentTemperatureField;
    public TextView weatherIcon;


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

        //cityField = (TextView)findViewById(R.id.city_field);
        //updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void renderWeather(JSONObject json){
        try {
         //   cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " +
         //           json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            detailsField.setText(
                    details.getString("description").toUpperCase(Locale.US) +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            currentTemperatureField.setText(
                    String.format("%.2f", main.getDouble("temp"))+ " ℃");

          //  DateFormat df = DateFormat.getDateTimeInstance();
         //   String updatedOn = df.format(new Date(json.getLong("dt")*1000));
         //   updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = this.getString(R.string.weather_sunny);
            } else {
                icon = this.getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = this.getString(R.string.weather_thunder);
                    break;
                case 3 : icon = this.getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = this.getString(R.string.weather_foggy);
                    break;
                case 8 : icon = this.getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = this.getString(R.string.weather_snowy);
                    break;
                case 5 : icon = this.getString(R.string.weather_rainy);
                    break;
            }
        }
        weatherIcon.setText(icon);
    }

}