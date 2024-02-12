package com.example.foodzarella.favmeals.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.Meal;


import java.util.List;

public interface FavMealsPresenter {
    public LiveData<List<Meal>> getMeals();
    public void deleteFromFav(Meal product);
}
