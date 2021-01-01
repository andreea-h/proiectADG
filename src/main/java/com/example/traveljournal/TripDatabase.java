package com.example.traveljournal;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Trip.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class TripDatabase extends RoomDatabase {
    public abstract TripDAO tripDAO();

    private static volatile TripDatabase instance;
    private static final int NUM_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUM_THREADS);

    public static TripDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (TripDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            TripDatabase.class, "trips_table").build();
                }
            }
        }
        return instance;
    }
}
