package com.example.foodzarella.dayMeal.view;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.DayMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface DayMealView {
    public void showData(Flowable<List<DayMeal>> listLiveData);
    public void showErrMsg(String error);
}
