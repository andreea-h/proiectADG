package com.example.traveljournal.myFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveljournal.R;
import com.example.traveljournal.RecyclerViewActivity;
import com.example.traveljournal.Trip;
import com.example.traveljournal.TripDatabase;
import com.example.traveljournal.TripRepository;
import com.example.traveljournal.TripViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    public static final int NEW_TRIP_ACTIVITY_REQUEST_CODE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    // Declare Context variable at class level in Fragment
    private static Context mContext;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static List<Trip> trips = new ArrayList<>();
    private static final HomeFragment instance = new HomeFragment();
    private TripViewModel tripViewModel;

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recycler_view, container, false);
        RecyclerView tripList_rv = view.findViewById(R.id.rv_trip_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        verifyStoragePermissions(this);
        tripList_rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        tripList_rv.addItemDecoration(divider);

        RecyclerViewActivity.ItemAdapter itemAdapter = new RecyclerViewActivity.ItemAdapter(trips);
        tripList_rv.setAdapter(itemAdapter);

        tripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);
        // Update the cached copy of the words in the adapter.
        tripViewModel.getAllTrips().observe(getViewLifecycleOwner(), itemAdapter::setTrips);
        return view;
    }

    public void verifyStoragePermissions(Fragment fragment) {
        //check read permission
        int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static void bookmarkPressed(Trip trip, ImageView iconView) {
        if (trip.getFavourite()) {
            iconView.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
            trip.setFavourite(false);
            //update info in database
            TripRepository.insertAsyncTask.execute(() -> {
                TripDatabase dataBase = TripDatabase.getDatabase(mContext);
                dataBase.tripDAO().updateTrip(trip);
            });
            Toast.makeText(mContext, "Trip removed for favourites", Toast.LENGTH_SHORT).show();
        } else {
            trip.setFavourite(true);
            iconView.setImageResource(R.drawable.ic_baseline_bookmark_24);
            //update info in database
            TripRepository.insertAsyncTask.execute(() -> {
                TripDatabase dataBase = TripDatabase.getDatabase(mContext);
                dataBase.tripDAO().updateTrip(trip);
            });
            Toast.makeText(mContext, "Trip marked as favourite", Toast.LENGTH_SHORT).show();
        }
    }


}
