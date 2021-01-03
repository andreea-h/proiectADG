package com.example.traveljournal;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TripViewModel extends AndroidViewModel {
    private TripRepository tripRepository;

    private final LiveData<List<Trip>> allTrips;

    private final LiveData<List<Trip>> favouritesTrips;

    public TripViewModel(Application application) {
        super(application);
        tripRepository = new TripRepository(application);
        allTrips = tripRepository.getAllTrips();
        favouritesTrips = tripRepository.getFavouritesTrips();
    }

    public LiveData<List<Trip>> getAllTrips() {
        return allTrips;
    }

    public LiveData<List<Trip>> getFavouritesTrips() {
        return favouritesTrips;
    }

    public void insert(Trip trip) {
        tripRepository.insert(trip);
    }

    public void updateTrip(Trip trip) {
        tripRepository.updateTrip(trip);
    }


}