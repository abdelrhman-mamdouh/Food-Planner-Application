package com.example.foodzarella.allmeals.view;

import static androidx.core.content.ContextCompat.startActivity;
import static com.example.foodzarella.SnackbarUtils.showSnackbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.R;
import com.example.foodzarella.SnackbarUtils;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.meal_details.MealDetailsActivity;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;
import com.example.foodzarella.ui.meal_plan.EventEditActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


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
        isFavorite = false;
    }

    @NonNull
    @Override
    public ViewHolderMeals onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item2, parent, false);
        return new ViewHolderMeals(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMeals holder, int position) {
        Meal meal = mealList.get(position);
        holder.titleTextView.setText(meal.getStrMeal());
        mealsLocalDataSource.isMealExists(meal.getIdMeal()).observe((LifecycleOwner) context, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean exists) {
                if (exists) {
                    holder.add.setImageResource(R.drawable.ic_favorite_on);
                    isFavorite = true;
                } else {
                    holder.add.setImageResource(R.drawable.ic_favorite_off);
                    isFavorite = false;
                }
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    holder.add.setImageResource(R.drawable.ic_favorite_off);
                    MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource)
                            .delete(meal);
                    showSnackbar(context, v, "Meal removed from favorites");
                } else {
                    holder.add.setImageResource(R.drawable.ic_favorite_on);
                    MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource)
                            .insert(meal);
                    showSnackbar(context, v, "Meal added to favorites");
                }
                isFavorite = !isFavorite;
            }
        });
        holder.MealPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayMeal dayMeal= new DayMeal(meal.getStrMeal(),meal.getStrMealThumb(),meal.getIdMeal());
                Gson gson = new Gson();
                String mealJson = gson.toJson(dayMeal);
                SharedPreferences sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("meal", mealJson); // Save the serialized Meal object
                editor.apply();

                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
                navController.navigate(R.id.navigation_meal_plan);
            }
        });
        String url = meal.getStrMealThumb();
        Glide.with(context)
                .load(url).apply(new RequestOptions().override(150, 150))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MealDetailsActivity.class);
                intent.putExtra("ID_KEY", meal.getIdMeal());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    public void setList(List<Meal> updatedProducts) {
        this.mealList = updatedProducts;
    }

}