package com.example.foodzarella.day_meal.view;

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
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;

import java.util.List;

public class DayMealAdapter extends RecyclerView.Adapter<DayMealViewHolder> {
    private List<DayMeal> mealList;
    private final Context context;
    MealsLocalDataSource mealsLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;

    public DayMealAdapter(List<DayMeal> dayMeals, Context context, MealsLocalDataSource localDataSource, MealsRemoteDataSource remoteDataSource) {
        this.mealList = dayMeals;
        this.context = context;
        this.mealsLocalDataSource = localDataSource;
        this.mealsRemoteDataSource = remoteDataSource;
    }

    @NonNull
    @Override
    public DayMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.favorite_meal, parent, false);
        return new DayMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayMealViewHolder holder, int position) {
        DayMeal currentMeal = mealList.get(position);
        holder.titleTextView.setText(currentMeal.getStrMeal());
        holder.remove.setOnClickListener(v -> {
         MealsRepositoryImol.getInstance(mealsRemoteDataSource,mealsLocalDataSource).deleteDayMeal(currentMeal);
            Toast.makeText(context, "Product removed from favorites", Toast.LENGTH_SHORT).show();
        });
        String url = currentMeal.getStrMealThumb();
        Glide.with(context)
                .load(url).apply(new RequestOptions().override(200,200))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.layout.setOnClickListener(view -> Toast.makeText(context, mealList.get(position).getStrMeal(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    } public void setList(List<DayMeal> updatedMeals){
        this.mealList =updatedMeals;
    }
}