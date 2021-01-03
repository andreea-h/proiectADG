package com.example.traveljournal.myFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveljournal.AddNewTripActivity;
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
    public static final String TRIP_ID = "tripID";
    public static final String TRIP_NAME = "name";
    public static final String TRIP_DESTINATION = "destination";
    public static final String TRIP_TYPE = "type";
    public static final String TRIP_PRICE = "price";
    public static final String TRIP_START_DATE = "start_date";
    public static final String TRIP_END_DATE = "end_date";
    public static final String TRIP_RATING = "rating";
    public static final String TRIP_PHOTO_PATH = "photo_path";
    public static final String TRIP_IS_FAV = "isFavourite";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //context variable
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
        // update the cached copy of the trips in the adapter.
        tripViewModel.getAllTrips().observe(getViewLifecycleOwner(), itemAdapter::setTrips);

        tripList_rv.addOnItemTouchListener(
                new RecyclerItemClickListener(mContext, tripList_rv ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        //open a new screen with details of the trip in read only mode

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //open edit action screen
                        Trip selectedTrip = tripViewModel.getAllTrips().getValue().get(position);

                        Intent intent = new Intent(view.getContext(), AddNewTripActivity.class);
                        intent.putExtra(TRIP_ID, selectedTrip.getTripID());
                        intent.putExtra(TRIP_NAME, selectedTrip.getName());
                        intent.putExtra(TRIP_DESTINATION, selectedTrip.getDestination());
                        intent.putExtra(TRIP_TYPE, selectedTrip.getType());
                        intent.putExtra(TRIP_PRICE, selectedTrip.getPrice());
                        intent.putExtra(TRIP_START_DATE, selectedTrip.getStartDate().getDay() + "/"
                                + selectedTrip.getStartDate().getMonth() + "/" + selectedTrip.getStartDate().getYear());
                        intent.putExtra(TRIP_END_DATE, selectedTrip.getEndDate().getDay() + "/"
                                + selectedTrip.getEndDate().getMonth() + "/" + selectedTrip.getEndDate().getYear());
                        intent.putExtra(TRIP_RATING, selectedTrip.getRating());
                        intent.putExtra(TRIP_PHOTO_PATH, selectedTrip.getImagePath());
                        intent.putExtra(TRIP_IS_FAV, selectedTrip.getFavourite());

                        view.getContext().startActivity(intent);
                    }
                })
        );
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    //add/remove favourite trip
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

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            public void onItemClick(View view, int position);

            public void onLongItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && mListener != null) {
                        mListener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
                return true;
            }
            return false;
        }

        @Override public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) { }

        @Override
        public void onRequestDisallowInterceptTouchEvent (boolean disallowIntercept){}
    }


}
