package com.example.foodzarella.db;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface MealsLocalDataSource {
    Flowable<List<Meal>> getMealList();

    Flowable<List<DayMeal>> getDayMealListByDate(String selectedDate);
    Flowable<List<DayMeal>> getDayMealList();

    void delete(Meal meal);

    void insert(Meal meal);

    void deleteDayMeal(DayMeal meal);

    void insertDayMeal(DayMeal meal);

    Flowable<Boolean> isMealExists(String mealId);

}
