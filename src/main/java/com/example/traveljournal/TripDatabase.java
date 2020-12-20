package com.example.traveljournal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Trip.class}, version = 1)
public abstract class TripDatabase extends RoomDatabase {
    public abstract TripDAO tripDAO();

    private static volatile TripDatabase instance;
    private static final int NUM_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_THREADS);

    static TripDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (TripDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            TripDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return instance;
    }
}
