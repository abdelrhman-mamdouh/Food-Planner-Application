package com.example.foodzarella.allmeals.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mvptask.R;


public class ViewHolderMeals extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleTextView;
    TextView priceTextView;
    TextView descriptionTextView;
    TextView brandTextView;
    RatingBar ratingBar;
    CardView layout;

    Button add;

    public ViewHolderMeals(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        titleTextView = itemView.findViewById(R.id.textView);
        priceTextView = itemView.findViewById(R.id.textView2);
        descriptionTextView = itemView.findViewById(R.id.textView3);
        brandTextView = itemView.findViewById(R.id.textView4);
        ratingBar = itemView.findViewById(R.id.ratingBar);
        layout = itemView.findViewById(R.id.cardView);
        add = itemView.findViewById(R.id.btnAdd);
    }
}
