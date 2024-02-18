package com.example.foodzarella.dayMeal.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;


public class DayMealViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView titleTextView;
    public CardView layout;
    public AppCompatImageButton remove;

    public DayMealViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_meal);
        titleTextView = itemView.findViewById(R.id.tv_meal_name);
        layout = itemView.findViewById(R.id.cardView);
        remove=itemView.findViewById(R.id.removeButton);
    }
}
