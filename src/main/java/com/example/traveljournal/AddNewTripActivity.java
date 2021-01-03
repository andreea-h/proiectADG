package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddNewTripActivity extends AppCompatActivity implements EventListener {
    public static final int SELECTPHOTO_REQUEST_CODE = 100;
    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    final Trip newTripItem = new Trip();

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

    private EditText tripNameEditText;
    private EditText tripDestinationEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);

        initInputs();

        priceSlider.setMax(1000);
        priceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("Valorea este: " + priceSlider.getProgress());
                priceTextView.setText("Price: " + priceSlider.getProgress() * 10 + " EUR");
                newTripItem.setPrice(priceSlider.getProgress() * 10);
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
        TripRepository.insertAsyncTask.execute(() -> {
            TripDatabase dataBase = TripDatabase.getDatabase(context);
            //add new trip info in local database
            dataBase.tripDAO().insert(newTripItem);
            List<Trip> db = dataBase.tripDAO().getAll().getValue();
        });

        Toast.makeText(this, "Trip successfully added", Toast.LENGTH_SHORT).show();

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