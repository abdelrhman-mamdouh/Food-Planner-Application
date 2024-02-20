package com.example.foodzarella.dayMeal.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface DayMealsPresenter {
    public Flowable<List<DayMeal>> getMeals();
    public Flowable<List<DayMeal>> getMealsByDate(String selectedDate);
    public void deleteDayMeal(DayMeal meal);
}
