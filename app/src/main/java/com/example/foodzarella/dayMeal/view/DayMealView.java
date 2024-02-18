package com.example.foodzarella.dayMeal.view;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;

import java.util.List;

public interface DayMealView {
    public void showData(LiveData <List<DayMeal>> listLiveData);
    public void showErrMsg(String error);
}
