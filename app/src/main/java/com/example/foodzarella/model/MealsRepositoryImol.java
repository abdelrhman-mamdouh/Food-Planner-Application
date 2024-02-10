package com.example.foodzarella.model;

import androidx.lifecycle.LiveData;


import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.network.MealsRemoteDataSource;
import com.example.foodzarella.network.NetworkCallback;

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
    public void getAllMeals(NetworkCallback networkCallback) {
        mealsRemoteDataSource.makeNetworkCall(networkCallback);
    }

    @Override
    public void delete(Meal meal) {
        mealsLocalDataSource.delete(meal);
    }

    @Override
    public void insert(Meal meal) {
      mealsLocalDataSource.insert(meal);
    }
}
