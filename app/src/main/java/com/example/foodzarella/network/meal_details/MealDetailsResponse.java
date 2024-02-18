package com.example.foodzarella.network.meal_details;

import com.example.foodzarella.mealDetails.MealDetails;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealDetailsResponse {
    @SerializedName("meals")
    private List<MealDetails> meals;

    public List<MealDetails> getMeals() {
        return meals;
    }
}