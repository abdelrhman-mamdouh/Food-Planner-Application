package com.example.foodzarella.day_meal.view;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;

import java.util.List;

public interface DayMealView {
    public void showData(LiveData <List<DayMeal>> listLiveData);
    public void showErrMsg(String error);
}
