package com.example.foodzarella.db;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.Meal;

import java.util.List;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource {
    private MealDAO mealDAO;
    private LiveData<List<Meal>> mealList;
    public static MealsLocalDataSource mealsLocalDataSource =null;

    private MealsLocalDataSourceImpl(Context context){
        MealDatabase db = MealDatabase.getInstance(context.getApplicationContext());
        mealDAO = db.getMealDAO();
        mealList = mealDAO.getAllMeals();
    }
    public static MealsLocalDataSource getInstance(Context context) {
        if (mealsLocalDataSource == null) {
            mealsLocalDataSource = new MealsLocalDataSourceImpl(context);
        }
        return mealsLocalDataSource;
    }
    @Override
    public LiveData<List<Meal>> getMealList() {
        return mealList;
    }

    @Override
    public void delete(Meal product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDAO.delete(product);
            }
        }).start();
    }

    @Override
    public void insert(Meal product) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDAO.insert(product);
            }
        }).start();
    }

}
