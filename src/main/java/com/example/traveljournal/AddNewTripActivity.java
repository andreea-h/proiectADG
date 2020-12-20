package com.example.traveljournal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddNewTripActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.wordlistsql.REPLY";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_trip);
    }
}