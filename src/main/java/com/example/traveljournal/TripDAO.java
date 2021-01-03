package com.example.traveljournal;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDAO {
    @Query("SELECT * from trips_table")
    LiveData<List<Trip>> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("DELETE FROM trips_table")
    void deleteAll();

    @Query("SELECT * FROM trips_table WHERE isFavourite IN (1)")
    LiveData<List<Trip>> getFavouritesTrips();

    @Update
    void updateTrip(Trip trip);


}
