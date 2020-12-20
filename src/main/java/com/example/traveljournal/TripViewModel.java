package com.example.traveljournal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;

    private final List<Trip> allTrips;

    public TripViewModel(Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTrips = tripRepository.getAllTrips();
    }

    public List<Trip> getAllTrips() {
        return allTrips;
    }

    public void insert(Trip trip) {
        tripRepository.insert(trip);
    }
}
