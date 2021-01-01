package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity { //trip list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
    }

    private static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView destination;
        private final TextView price;
        public ImageView tripImage;
        public ImageView bookmark;
        private final TextView rating;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.trip_name);
            destination = itemView.findViewById(R.id.trip_destination);
            price = itemView.findViewById(R.id.trip_price);
            tripImage = itemView.findViewById(R.id.image);
            bookmark = itemView.findViewById(R.id.bookmark);
            rating = itemView.findViewById(R.id.trip_rating);
        }

        @SuppressLint("SetTextI18n")
        void bind(@NonNull final Trip item) {
            name.setText(item.getName());
            destination.setText("Destination -> " + item.getDestination());
            price.setText("Price -> " + String.valueOf(item.getPrice()) +"euro");
            rating.setText("Rating -> " + String.valueOf(item.getRating()) + "/5");
        }
    }

    public static class ItemAdapter extends RecyclerView.Adapter<RecyclerViewActivity.ItemViewHolder> {

        @NonNull
        private final List<Trip> items;

        public ItemAdapter(@NonNull List<Trip> items) {
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