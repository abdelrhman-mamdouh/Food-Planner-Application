package com.example.foodzarella.day_meal.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;

import java.util.List;

public interface DayMealsPresenter {
    public LiveData<List<DayMeal>> getMeals();

    public void deleteDayMeal(DayMeal meal);
}
