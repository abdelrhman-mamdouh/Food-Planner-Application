package com.example.foodzarella.network.getMeals;


import com.example.foodzarella.model.Meal;

import java.util.List;

public interface NetworkCallback {
    void onSuccessResult(List<Meal> mealList);
    void onFailureResult(String errorMsg);
}
