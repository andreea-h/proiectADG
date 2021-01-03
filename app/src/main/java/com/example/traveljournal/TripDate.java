package com.example.traveljournal;

//represent start/end trip date
public class TripDate {
    private int day;
    private int month;
    private int year;

    public TripDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    void setDay(int day) {
        this.day = day;
    }

    void setMonth(int month) {
        this.month = month;
    }

    void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

}
