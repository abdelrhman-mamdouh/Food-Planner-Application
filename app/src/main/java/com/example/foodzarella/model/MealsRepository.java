package com.example.foodzarella.model;

import androidx.lifecycle.LiveData;


import com.example.foodzarella.network.NetworkCallback;

import java.util.List;

public interface MealsRepository {
    public LiveData<List<Meal>> getMealList() ;
    void getAllMeals(NetworkCallback networkCallback);

    void delete(Meal product);

    void insert(Meal product);
}
