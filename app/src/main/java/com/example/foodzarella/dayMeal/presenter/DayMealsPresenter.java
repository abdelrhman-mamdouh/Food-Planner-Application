package com.example.foodzarella.dayMeal.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;

import java.util.List;

public interface DayMealsPresenter {
    public LiveData<List<DayMeal>> getMeals();
    public LiveData<List<DayMeal>> getMealsByDate(String selectedDate);
    public void deleteDayMeal(DayMeal meal);
}
