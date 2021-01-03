package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveljournal.myFragments.HomeFragment;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.StringTokenizer;

public class AddNewTripActivity extends AppCompatActivity implements EventListener {
    public static final int SELECTPHOTO_REQUEST_CODE = 100;

    Trip newTripItem;

    private TextInputLayout tripNameTextInputLayout;
    private TextInputLayout tripDestinationTextInputLayout;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private SeekBar priceSlider;
    private TextView priceTextView;
    private RatingBar ratingBar;

    private String photoPath;
    private TextView imagePathTextView;

    private RadioGroup tripTypeOptions;
    private RadioButton tripType;

    private RadioButton city_break_button;
    private RadioButton sea_side_button;
    private RadioButton mountains_button;

    private EditText tripNameEditText;
    private EditText tripDestinationEditText;
    private Button submitButton;

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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);

        initInputs();

        //for edit action, check data received from HomeFragment
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


        if (tripId != null) {
            //EDIT ACTION
            //init fields with data about specific trip extracted from database
            tripNameEditText.setText(tripName, TextView.BufferType.EDITABLE);
            tripDestinationEditText.setText(tripDestination, TextView.BufferType.EDITABLE);

            //String name, String destination, String type, float price, TripDate startDate, TripDate endDate, float rating, String imagePath, boolean isFavourite
            switch (tripTypeAsString) {
                case "City Break":
                    city_break_button.setChecked(true);
                    break;
                case "Sea side":
                    sea_side_button.setChecked(true);
                    break;
                case "Mountains":
                    mountains_button.setChecked(true);
                    break;
            }

            priceSlider.setProgress((int)tripPrice);
            ratingBar.setRating(tripRating);
            photoPath = photoPathString;
            imagePathTextView.setText(photoPathString);
            priceTextView.setText(" " + tripPrice + "euro");

            StringTokenizer startDateSt = new StringTokenizer(startData, "/");
            int i = 0;
            int day = 0, month = 0, year = 0;
            while (startDateSt.hasMoreTokens()) {
                if (i == 0) {
                    day = Integer.parseInt(startDateSt.nextToken());
                }
                if (i == 1) {
                    month = Integer.parseInt(startDateSt.nextToken());
                }
                if (i == 2) {
                    year = Integer.parseInt(startDateSt.nextToken());
                }
                i++;
            }
            startDatePicker.updateDate(year, month, day);
            TripDate startDate = new TripDate(day, month, year);

            i = 0;
            StringTokenizer endDateSt = new StringTokenizer(endData, "/");
            while (endDateSt.hasMoreTokens()) {
                if (i == 0) {
                    day = Integer.parseInt(endDateSt.nextToken());
                }
                if (i == 1) {
                    month = Integer.parseInt(endDateSt.nextToken());
                }
                if (i == 2) {
                    year = Integer.parseInt(endDateSt.nextToken());
                }
                i++;
            }
            endDatePicker.updateDate(year, month, day);
            TripDate endDate = new TripDate(day, month, year);
            newTripItem = new Trip(tripName, tripDestination, tripTypeAsString, tripPrice, startDate, endDate, tripRating, photoPathString, isFavourite);
            newTripItem.setTripID(tripId);
        }
        else {
            //ADD TRIP ACTION
            newTripItem = new Trip();
        }

        priceSlider.setMax(1500);
        priceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                priceTextView.setText("     " + priceSlider.getProgress() + " euro");
                newTripItem.setPrice(priceSlider.getProgress());
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void btnSaveOnClick(View view) throws ParseException {
        getInputs();

        if (!validation()) {
            return;
        }
        //updateDB
        Context context = getApplicationContext();
        //access localDatabase
        if (tripId == null) {
            //ADD TRIP ACTION
            TripRepository.insertAsyncTask.execute(() -> {
                TripDatabase dataBase = TripDatabase.getDatabase(context);
                //add new trip info in local database
                dataBase.tripDAO().insert(newTripItem);
            });

            Toast.makeText(this, "Trip successfully added", Toast.LENGTH_SHORT).show();
        }
        else {
            //EDIT TRIP ACTION
            TripRepository.insertAsyncTask.execute(() -> {
                TripDatabase dataBase = TripDatabase.getDatabase(context);
                //add new trip info in local database
                dataBase.tripDAO().updateTrip(newTripItem);
            });
            Toast.makeText(this, "Trip successfully updated", Toast.LENGTH_SHORT).show();
        }

        //back to main screen
        Intent mainIntent = new Intent(AddNewTripActivity.this, MainScreenActivity.class);
        AddNewTripActivity.this.startActivity(mainIntent);
        AddNewTripActivity.this.finish();
    }

    //get photo trip
    public void btnSelectPhotoOnClick(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select photo");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, SELECTPHOTO_REQUEST_CODE);
    }

    //get image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECTPHOTO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                photoPath = cursor.getString(columnIndex);
                cursor.close();

                imagePathTextView.setText(photoPath);
            }
        }
    }

    private void getInputs() {
        //set trip name and destination
        newTripItem.setName(getNameFromEditText());
        newTripItem.setDestination(getDestinationFromEditText());

        //get and set tripType
        int selectedId = tripTypeOptions.getCheckedRadioButtonId();
        tripType = findViewById(selectedId);
        newTripItem.setType(tripType.getText().toString());

        //get start, end date from DatePickers
        int start_day = startDatePicker.getDayOfMonth();
        int start_month = startDatePicker.getMonth();
        int start_year = startDatePicker.getYear();
        newTripItem.setStartDate(start_day, start_month, start_year);

        int end_day = endDatePicker.getDayOfMonth();
        int end_month = endDatePicker.getMonth();
        int end_year = endDatePicker.getYear();
        newTripItem.setEndDate(end_day, end_month, end_year);

        //get trip rating
        float ratingVal = ratingBar.getRating();
        newTripItem.setRating(ratingVal);
        newTripItem.setImagePath(imagePathTextView.getText().toString());
    }

    private String getNameFromEditText() {
       return tripNameEditText.getText().toString();
    }

    private String getDestinationFromEditText() {
        return tripDestinationEditText.getText().toString();
    }

    private void initInputs() {
        tripNameTextInputLayout = findViewById(R.id.til_trip_name);
        tripDestinationTextInputLayout = findViewById(R.id.til_trip_destination);

        tripNameEditText = findViewById(R.id.et_trip_name);
        tripDestinationEditText = findViewById(R.id.et_trip_destination);

        startDatePicker = findViewById(R.id.start_date);
        endDatePicker = findViewById(R.id.end_date);

        priceSlider = findViewById(R.id.price);
        ratingBar = findViewById(R.id.rating);
        priceTextView = findViewById(R.id.price_textView);

        tripTypeOptions = findViewById(R.id.trip_type);
        submitButton = findViewById(R.id.submit_button);
        imagePathTextView = findViewById(R.id.image_path_textView);

        city_break_button = findViewById(R.id.city_break);
        sea_side_button = findViewById(R.id.sea_side);
        mountains_button = findViewById(R.id.mountains);
    }

    //return false if not all fields are completed
    private boolean validation() throws ParseException {
        if(tripNameEditText.getText().toString() == null || tripNameEditText.getText().toString().isEmpty()) {
            Toast.makeText(AddNewTripActivity.this, "Enter trip name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( tripDestinationEditText.getText().toString() == null ||  tripDestinationEditText.getText().toString().isEmpty()) {
            Toast.makeText(AddNewTripActivity.this, "Enter destination", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(tripTypeOptions.getCheckedRadioButtonId() == -1) {
            Toast.makeText(AddNewTripActivity.this, "Select trip type", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(priceSlider.getProgress() == 0) {
            Toast.makeText(AddNewTripActivity.this, "Select trip price", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startDatePicker == null) {
            Toast.makeText(AddNewTripActivity.this, "Enter start date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(endDatePicker == null) {
            Toast.makeText(AddNewTripActivity.this, "Enter end date", Toast.LENGTH_SHORT).show();
            return false;
        }
        SimpleDateFormat format = new SimpleDateFormat("d/M/y");
        Date sDate = format.parse(startDatePicker.getDayOfMonth() + "/" + startDatePicker.getMonth() + "/" + startDatePicker.getYear());
        Date eDate = format.parse(endDatePicker.getDayOfMonth() + "/" + endDatePicker.getMonth() + "/" + endDatePicker.getYear());
        if (eDate.compareTo(sDate) < 0) {
            Toast.makeText(AddNewTripActivity.this, "End date occurs before start date!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(photoPath == null) {
            Toast.makeText(AddNewTripActivity.this, "Select photo", Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }
}