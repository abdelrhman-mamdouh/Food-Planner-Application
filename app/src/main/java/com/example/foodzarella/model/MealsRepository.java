package com.example.foodzarella.model;


import com.example.foodzarella.network.getMeals.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface MealsRepository {
    public Flowable<List<Meal>> getMealList() ;

    public Flowable<List<DayMeal>> getDayMealList() ;

    public Flowable<List<DayMeal>> getDayMealListByDate(String selectedDate) ;
    void getAllMeals(NetworkCallback networkCallback,String category);

    void delete(Meal product);

    void insert(Meal product);

    void deleteDayMeal(DayMeal dayMeal);

    void insertDayMeal(DayMeal dayMeal);


}
