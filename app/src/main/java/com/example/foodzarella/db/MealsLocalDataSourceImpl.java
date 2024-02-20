package com.example.foodzarella.db;

import android.content.Context;

import androidx.lifecycle.LiveData;


import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class MealsLocalDataSourceImpl implements MealsLocalDataSource {
    private MealDAO mealDAO;
    private Flowable<List<Meal>> mealList;

    private DayMealDao dayMealDao;
    private Flowable<List<DayMeal>> dayMealsList;

    public static MealsLocalDataSource mealsLocalDataSource = null;

    private MealsLocalDataSourceImpl(Context context) {
        MealDatabase db = MealDatabase.getInstance(context.getApplicationContext());
        mealDAO = db.getMealDAO();
        mealList = mealDAO.getAllMeals();
        dayMealDao = db.getDayMealDAO();
        dayMealsList= dayMealDao.getAllDayMeals();
    }

    public static MealsLocalDataSource getInstance(Context context) {
        if (mealsLocalDataSource == null) {
            mealsLocalDataSource = new MealsLocalDataSourceImpl(context);
        }
        return mealsLocalDataSource;
    }

    @Override
    public Flowable<List<Meal>> getMealList() {
        return mealList;
    }

    @Override
    public Flowable<List<DayMeal>> getDayMealListByDate(String selectedDate) {
        return dayMealDao.getDayMealListByDate(selectedDate);
        }


    @Override
    public Flowable<List<DayMeal>> getDayMealList() {
        return dayMealsList;
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

    @Override
    public void deleteDayMeal(DayMeal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dayMealDao.delete(meal);
            }
        }).start();
    }

    @Override
    public void insertDayMeal(DayMeal meal) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dayMealDao.insert(meal);
            }
        }).start();
    }


    @Override
    public Flowable<Boolean> isMealExists(String mealId) {
        return mealDAO.isMealExists(mealId);
    }



}
