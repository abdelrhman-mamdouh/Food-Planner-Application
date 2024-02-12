package com.example.foodzarella.favmeals.view;

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


public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleTextView;
    CardView layout;
    AppCompatImageButton remove;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_meal);
        titleTextView = itemView.findViewById(R.id.tv_meal_name);
        layout = itemView.findViewById(R.id.cardView);
        remove=itemView.findViewById(R.id.removeButton);
    }
}
