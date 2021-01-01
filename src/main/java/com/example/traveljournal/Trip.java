package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "trips_table") //Trip class represents a SQLite table
public class Trip {
    @NonNull
    @PrimaryKey
    private String name;
    @ColumnInfo(name = "destination")
    private String destination;
    @ColumnInfo(name = "type")
    private String type; //3 possible values
    @ColumnInfo(name = "price")
    private float price;
    @ColumnInfo(name = "startData")
    private TripDate startDate;
    @ColumnInfo(name = "endDate")
    private TripDate endDate;
    @ColumnInfo(name = "rating")
    private float rating;
    private String imagePath;
    private boolean isFavourite;



    public Trip() {
        this("", "", "", 0, new TripDate(0,0,0), new TripDate(0,0,0), 0, "", false);
    }

    public Trip(@NotNull String name, String destination, String type, float price, TripDate startDate, TripDate endDate, float rating, String imagePath, boolean isFavourite) {
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
        this.isFavourite = isFavourite;
        this.imagePath = imagePath;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setStartDate(TripDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(TripDate endDate) {
        this.endDate = endDate;
    }

    public void setStartDate(int day, int month, int year) {
       startDate = new TripDate(day, month, year);
    }

    public void setEndDate(int day, int month, int year) {
        endDate = new TripDate(day, month, year);
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

    public String getType() {
        return type;
    }

    public float getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public TripDate getStartDate() {
        return startDate;
    }

    public TripDate getEndDate() {
        return endDate;
    }

    public boolean getFavourite() {
        return isFavourite;
    }

    public String getImagePath() {
        return imagePath;
    }

}
