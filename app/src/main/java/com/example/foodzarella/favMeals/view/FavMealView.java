package com.example.foodzarella.favMeals.view;

import com.example.foodzarella.model.Meal;


import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface FavMealView {
    public void showData(Flowable<List<Meal>> products);
    public void showErrMsg(String error);
}
