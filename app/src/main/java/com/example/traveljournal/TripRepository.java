package com.example.traveljournal;

import android.app.Application;
import android.os.AsyncTask;


import androidx.lifecycle.LiveData;

import java.util.List;

public class TripRepository {

    private TripDAO tripDao;
    private LiveData<List<Trip>> tripsData;
    private LiveData<List<Trip>> favouritesTrips;

    TripRepository(Application application) {
        TripDatabase db = TripDatabase.getDatabase(application);
        tripDao = db.tripDAO();
        tripsData = tripDao.getAll();
        favouritesTrips = tripDao.getFavouritesTrips();
    }

    LiveData<List<Trip>> getAllTrips() {
        return tripsData;
    }

    LiveData<List<Trip>> getFavouritesTrips() {
        return favouritesTrips;
    }

    void insert(Trip trip) {
        TripDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.insert(trip);
        });
    }

    void updateTrip(Trip trip) {
        TripDatabase.databaseWriteExecutor.execute(() -> {
            tripDao.updateTrip(trip);
        });
    }

    public static class insertAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDAO mAsyncTaskDao;

        insertAsyncTask(TripDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}