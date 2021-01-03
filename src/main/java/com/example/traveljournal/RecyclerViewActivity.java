package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveljournal.myFragments.HomeFragment;

import java.io.File;
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
        private final TextView rating;
        private final ImageView tripImage;
        private final ImageView bookmark;

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
            destination.setText(" " + item.getDestination());
            price.setText(" " + item.getPrice() +"euro");
            rating.setText(item.getRating() + "/5.0");
            File imageFile = new File(item.getImagePath());
            if(imageFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                tripImage.setImageBitmap(myBitmap);
                tripImage.setClipToOutline(true);
            }
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new RecyclerViewActivity().bookmarkPressed(item, bookmark);
                }
            });
        }
    }

    public void bookmarkPressed(Trip trip, ImageView iconView) {
        if (trip.getFavourite() == true) {
            //update info in database
            iconView.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
           /* AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    tjdb.tripDao().deleteTrip(trip.toObject(Trip.class));
                }
            });*/
            Toast.makeText(this, "Trip removed for favourites", Toast.LENGTH_SHORT).show();
        } else {
            //update info in database
            iconView.setImageResource(R.drawable.ic_baseline_bookmark_24);
           /* AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    tjdb.tripDao().addTrip(trip.toObject(Trip.class));
                }
            });*/
            Toast.makeText(this, "Trip marked as favourite", Toast.LENGTH_SHORT).show();
        }
    }

    public static class ItemAdapter extends RecyclerView.Adapter<RecyclerViewActivity.ItemViewHolder> {

        @NonNull
        private List<Trip> items;

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

        public void setTrips(List<Trip> items) {
            this.items = items;
            notifyDataSetChanged();
        }

    }
}