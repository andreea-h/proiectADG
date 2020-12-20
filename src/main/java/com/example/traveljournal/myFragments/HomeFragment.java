package com.example.traveljournal.myFragments;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
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

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private TripViewModel tripViewModel;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.activity_recycler_view, container, false);

        RecyclerView tripList_rv = view.findViewById(R.id.rv_trip_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        tripList_rv.setLayoutManager(linearLayoutManager);

        //extract trip list from local database
        LiveData<List<Trip>> dataSource = tripViewModel.getAllTrips();
        ItemAdapter itemAdapter = new ItemAdapter(dataSource.getValue());
        tripList_rv.setAdapter(itemAdapter);

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
        tripViewModel.getAllTrips().observe(getViewLifecycleOwner(), trips -> itemAdapter.submitList(trips));

        TripDatabase db = Room.databaseBuilder(getActivity().getApplicationContext(),
               TripDatabase.class, "trip-database").build();


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Trip trip = new Trip(data.getStringExtra(AddNewTripActivity.EXTRA_REPLY));
            tripViewModel.insert(trip);
        } else {
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView destination;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name);
            destination = itemView.findViewById(R.id.trip_destination);
        }

        void bind(@NonNull final Trip item) {
            name.setText(item.getName());
            destination.setText(item.getDestination());
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<RecyclerViewActivity.ItemViewHolder> {

        @NonNull
        private final List<Trip> items;

        ItemAdapter(@NonNull List<Trip> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public RecyclerViewActivity.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.fragment_home, parent, false);
            return new RecyclerViewActivity.ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewActivity.ItemViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

    }

}
