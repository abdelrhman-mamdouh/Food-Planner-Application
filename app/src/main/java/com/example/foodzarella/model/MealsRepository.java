package com.example.foodzarella.model;

import androidx.lifecycle.LiveData;


import com.example.foodzarella.network.get_meals.NetworkCallback;

import java.util.List;

public interface MealsRepository {
    public LiveData<List<Meal>> getMealList() ;
    void getAllMeals(NetworkCallback networkCallback,String category);

    void delete(Meal product);

    void insert(Meal product);
}
