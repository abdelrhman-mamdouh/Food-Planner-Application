package com.example.foodzarella.model;

import androidx.lifecycle.LiveData;



import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;
import com.example.foodzarella.network.get_meals.NetworkCallback;

import java.util.List;


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


    public LiveData<List<Meal>> getMealList() {
        return mealsLocalDataSource.getMealList();
    }

    @Override
    public LiveData<List<DayMeal>> getDayMealList() {
        return mealsLocalDataSource.getDayMealList();
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
