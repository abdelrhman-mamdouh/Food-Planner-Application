package com.example.foodzarella.allmeals.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.R;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.MealsRemoteDataSource;


import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<ViewHolderMeals> {
    private List<Meal> mealList;
    private final Context context;
    boolean isFavorite;
    MealsLocalDataSource mealsLocalDataSource;
   MealsRemoteDataSource mealsRemoteDataSource;
    public MealAdapter(List<Meal> mealList, Context context, MealsLocalDataSource localDataSource, MealsRemoteDataSource remoteDataSource) {
        this.mealList = mealList;
        this.context = context;
        this.mealsLocalDataSource = localDataSource;
        this.mealsRemoteDataSource = remoteDataSource;
        isFavorite=false;
    }

    @NonNull
    @Override
    public ViewHolderMeals onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item, parent, false);
        return new ViewHolderMeals(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMeals holder, int position) {
        Meal meal = mealList.get(position);
        holder.titleTextView.setText(meal.getStrMeal());
       // holder.ratingBar.setRating((float) meal.getRating());
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    holder.add.setImageResource(R.drawable.ic_favorite_off);
                } else {
                    holder.add.setImageResource(R.drawable.ic_favorite_on);
                }
                isFavorite = !isFavorite;
                MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource)
                                    .insert(meal);
                    Toast.makeText(context, "Meal added to favorites", Toast.LENGTH_SHORT).show();

            }
        });
        String url = meal.getStrMealThumb();
        Glide.with(context)
                .load(url).apply(new RequestOptions().override(200,200))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, mealList.get(position).getStrMeal(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    public void setList(List<Meal> updatedProducts){
        this.mealList =updatedProducts;
    }
}