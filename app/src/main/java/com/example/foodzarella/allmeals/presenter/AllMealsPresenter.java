package com.example.foodzarella.allmeals.presenter;


import com.example.foodzarella.model.Meal;

public interface AllMealsPresenter {
    public void getMeals();
    public void addToFav(Meal meal);
}
