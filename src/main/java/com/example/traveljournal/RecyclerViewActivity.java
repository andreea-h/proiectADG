package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity { //trip list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView tripList_rv = findViewById(R.id.rv_trip_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        tripList_rv.setLayoutManager(linearLayoutManager);

        //extract trip list from local database
        List<Trip> dataSource = Trip.getList();
        tripList_rv.setAdapter(new ItemAdapter(dataSource));
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView destination;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name);
            destination = itemView.findViewById(R.id.trip_destination);
        }

        public void bind(@NonNull final Trip item) {
            name.setText(item.getName());
            destination.setText(item.getDestination());
        }
    }

    private static class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder> {

        @NonNull
        private final List<Trip> items;

        ItemAdapter(@NonNull List<Trip> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.fragment_home, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
            holder.bind(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
}