package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class AddNewTripActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";

    private TextInputLayout tripNameTextInputLayout;
    private TextInputLayout tripDestinationTextInputLayout;

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private Slider priceSlider;
    private RatingBar ratingBar;

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get new trip info from input fields
                //and add new trip object info into the local database
                getInputsAndUpdateDB();
            }
        });
    }

    private void getInputsAndUpdateDB() {
        //generate new Trip Object
        final Trip newTripItem = new Trip();

        //set trip name and destination
        newTripItem.setName(getNameFromEditText());
        newTripItem.setDestination(getDestinationFromEditText());

        //get and set tripType
        int selectedId = tripTypeOptions.getCheckedRadioButtonId();
        tripType = findViewById(selectedId);
        newTripItem.setType(tripType.getText().toString());

        //get and set price value for new Trip object
        priceSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                newTripItem.setPrice(value);
            }
        });

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

        Context context = getApplicationContext();
        //access localDatabase
        AsyncTask.execute(() -> {
            TripDatabase dataBase = TripDatabase.getDatabase(context);
            //add new trip info in local database
            dataBase.tripDAO().insert(newTripItem);
            List<Trip> db = dataBase.tripDAO().getAll();
       });
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

        tripTypeOptions = findViewById(R.id.trip_type);
        submitButton = findViewById(R.id.submit_button);
    }

    //return false if not all fields are completed
    private boolean validation() {
        boolean valid = true;
        if (tripNameEditText.getText().toString().isEmpty()) {
            tripNameTextInputLayout.setError(getString(R.string.name_cannot_be_empty));
            valid = false;
        }
        if (tripDestinationEditText.getText().toString().isEmpty()) {
            tripDestinationEditText.setError(getString(R.string.destination_cannot_be_empty));
            valid = false;
        }
        return valid;
    }
}