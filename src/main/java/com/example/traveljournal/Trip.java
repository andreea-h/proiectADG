package com.example.traveljournal;

import java.util.ArrayList;
import java.util.List;

public class Trip {
    private String name;
    private String destination;
    private String type; //3 possible values
    private float price;
    private TripDate startDate;
    private TripDate endDate;
    private float rating;

    public Trip(String name, String destination, String type, float price, TripDate startDate, TripDate endDate, float rating) {
        this.name = name;
        this.destination = destination;
        this.type = type;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rating = rating;
    }

    public static List<Trip> getList() {
        int i;
        List<Trip> list = new ArrayList<Trip>(10);
        for (i = 0; i < 10; i++) {
            Trip t = new Trip("Trip " + i, "dest " + i, "tip", i,
                        new TripDate(0, 0, 0), new TripDate(0, 0, 0), 0);
            list.add(t);
        }
        return list;
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

    public void setPrice(float price) {
        this.price = price;
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
}
