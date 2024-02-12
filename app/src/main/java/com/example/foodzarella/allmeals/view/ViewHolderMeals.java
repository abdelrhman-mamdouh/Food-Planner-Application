package com.example.foodzarella.allmeals.view;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;


public class ViewHolderMeals extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleTextView;
    RatingBar ratingBar;
    CardView layout;

    AppCompatImageButton add;

    public ViewHolderMeals(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_meal);
        titleTextView = itemView.findViewById(R.id.tv_meal_name);
        layout = itemView.findViewById(R.id.cardView);
        add = itemView.findViewById(R.id.favoriteButton);
    }
}
