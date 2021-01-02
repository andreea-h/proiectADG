package com.example.traveljournal;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TripDAO {
    @Query("SELECT * from trips_table")
    List<Trip> getAll();

    @Query("SELECT * FROM trips_table WHERE name IN (:userIds)")
    List<Trip> loadAllByIds(int[] userIds);

    @Insert(onConflict = OnConflictStrategy.IGNORE) //daca se inceraca adaugarea unui trip cu acelasi nume, acesta va fi ignorat
    void insert(Trip trip);

    @Delete
    void delete(Trip trip);

    @Query("DELETE FROM trips_table")
    void deleteAll();
}
