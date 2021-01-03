package com.example.traveljournal;


import androidx.room.TypeConverter;

import com.google.gson.Gson;

public class Converters {

    //convert java object to its json representation
    @TypeConverter
    public String fromTripDateToGson(TripDate date) {
        Gson gson = new Gson();
        return gson.toJson(date);
    }

    @TypeConverter
    public TripDate fromGsonToTripDate(String stringDate) {
        Gson gson = new Gson();
        return gson.fromJson(stringDate, TripDate.class);
    }
}
