package com.example.traveljournal.myFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.traveljournal.AddNewTripActivity;
import com.example.traveljournal.R;
import com.example.traveljournal.RecyclerViewActivity;
import com.example.traveljournal.Trip;
import com.example.traveljournal.TripDAO;
import com.example.traveljournal.TripDatabase;
import com.example.traveljournal.TripViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    public static List<Trip> trips = new ArrayList<>();
    private RecyclerViewActivity instance = new RecyclerViewActivity();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        verifyStoragePermissions(this);
        View view = inflater.inflate(R.layout.activity_recycler_view, container, false);
        RecyclerView tripList_rv = view.findViewById(R.id.rv_trip_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        tripList_rv.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        tripList_rv.addItemDecoration(divider);

        //extract trip list from room database
        Context context = getActivity();
        AsyncTask.execute(() -> {
            TripDatabase dataBase = TripDatabase.getDatabase(context.getApplicationContext());
            trips = new ArrayList<Trip>(dataBase.tripDAO().getAll());
        });

        RecyclerViewActivity.ItemAdapter itemAdapter = new RecyclerViewActivity.ItemAdapter(trips);
        tripList_rv.setAdapter(itemAdapter);
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


}
