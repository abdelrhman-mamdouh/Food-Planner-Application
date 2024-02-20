package com.example.foodzarella.allMeals.presenter;


import com.example.foodzarella.model.Meal;

public interface AllMealsPresenter {
    public void getMeals();
    public void getMealsByCategory();
    public void getMealsByCountry();
    public void addToFav(Meal meal);
}
