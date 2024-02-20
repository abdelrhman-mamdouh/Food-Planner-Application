package com.example.foodzarella.model;


import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.network.getMeals.MealsRemoteDataSource;
import com.example.foodzarella.network.getMeals.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


public class MealsRepositoryImol implements MealsRepository {
    MealsLocalDataSource mealsLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;

    public static MealsRepository repository=null;
    public static MealsRepository getInstance(MealsRemoteDataSource mealsRemoteDataSource, MealsLocalDataSource mealsLocalDataSource) {
        if (repository == null) {
            repository = new MealsRepositoryImol(mealsRemoteDataSource, mealsLocalDataSource);
        }
        return repository;
    }
    private MealsRepositoryImol(MealsRemoteDataSource mealsRemoteDataSource, MealsLocalDataSource mealsLocalDataSource) {
       this.mealsRemoteDataSource=mealsRemoteDataSource;
       this.mealsLocalDataSource =mealsLocalDataSource;
    }


    public Flowable<List<Meal>> getMealList() {
        return mealsLocalDataSource.getMealList();
    }

    @Override
    public Flowable<List<DayMeal>> getDayMealList() {
        return mealsLocalDataSource.getDayMealList();
    }

    @Override
    public Flowable<List<DayMeal>> getDayMealListByDate(String selectedDate) {
        return mealsLocalDataSource.getDayMealListByDate(selectedDate);
    }


    @Override
    public void getAllMeals(NetworkCallback networkCallback,String category) {
        mealsRemoteDataSource.makeNetworkCall(networkCallback,category);
    }

    @Override
    public void delete(Meal product) {
        mealsLocalDataSource.delete(product);
    }

    @Override
    public void insert(Meal product) {
        mealsLocalDataSource.insert(product);
    }

    @Override
    public void deleteDayMeal(DayMeal dayMeal) {
        mealsLocalDataSource.deleteDayMeal(dayMeal);
    }

    @Override
    public void insertDayMeal(DayMeal dayMeal) {
        mealsLocalDataSource.insertDayMeal(dayMeal);
    }


}
