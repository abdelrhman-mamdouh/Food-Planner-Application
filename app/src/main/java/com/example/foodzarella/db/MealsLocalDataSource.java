package com.example.foodzarella.db;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.Meal;

import java.util.List;

public interface MealsLocalDataSource {
    LiveData<List<Meal>> getMealList();

    void delete(Meal meal);

    void insert(Meal meal);
}
