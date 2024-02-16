package com.example.foodzarella.db;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;

import java.util.List;

public interface MealsLocalDataSource {
    LiveData<List<Meal>> getMealList();

    LiveData<List<DayMeal>> getDayMealListByDate(String selectedDate);
    LiveData<List<DayMeal>> getDayMealList();

    void delete(Meal meal);

    void insert(Meal meal);

    void deleteDayMeal(DayMeal meal);

    void insertDayMeal(DayMeal meal);

    LiveData<Boolean> isMealExists(String mealId);

}
