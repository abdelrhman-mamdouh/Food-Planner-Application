package com.example.foodzarella.allMeals.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;


public class ViewHolderMeals extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView titleTextView;
    AppCompatImageButton MealPlan;
    CardView layout;

    AppCompatImageButton add;

    public ViewHolderMeals(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.iv_meal);
        titleTextView = itemView.findViewById(R.id.tv_meal_name);
        layout = itemView.findViewById(R.id.cardView);
        add = itemView.findViewById(R.id.favoriteButton);
        MealPlan=itemView.findViewById(R.id.btn_meal_plan);
    }
}
