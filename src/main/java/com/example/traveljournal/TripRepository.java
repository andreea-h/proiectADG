package com.example.traveljournal;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {

    private TripDAO tripDao;
    private LiveData<List<Trip>> tripsData;

    TripRepository(Application application) {
        TripDatabase db = TripDatabase.getDatabase(application);
        tripDao = db.tripDAO();
        tripsData = tripDao.getAll();
    }

    LiveData<List<Trip>> getAllTrips() {
        return tripsData;
    }

    void insert(Trip trip) {
        TripDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.insert(trip);
        });
    }

}
