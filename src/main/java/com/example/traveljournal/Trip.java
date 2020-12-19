package com.example.traveljournal;

public class Trip {
    private String name;
    private String destination;
    private String type; //3 possible values
    private float price;
    private TripDate startDate;
    private TripDate endDate;

    private float rating;

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
