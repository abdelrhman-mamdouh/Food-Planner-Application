package com.example.foodzarella.favMeals.presenter;

import com.example.foodzarella.model.Meal;


import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface FavMealsPresenter {
    public Flowable<List<Meal>> getMeals();
    public void deleteFromFav(Meal product);
}
