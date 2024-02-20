package com.example.foodzarella.mealDetails.view;

import com.example.foodzarella.mealDetails.model.MealDetails;

public interface MealDetailsView {
    void displayMealDetails(MealDetails mealDetails);
    void showErrorMessage();
}