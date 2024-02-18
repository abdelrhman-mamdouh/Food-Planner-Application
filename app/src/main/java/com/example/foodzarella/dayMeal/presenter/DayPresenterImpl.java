package com.example.foodzarella.dayMeal.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.dayMeal.view.DayMealView;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.MealsRepository;

import java.util.List;

public class DayPresenterImpl implements DayMealsPresenter {
    private DayMealView _view;
    private MealsLocalDataSource mealsLocalDataSource;
    private MealsRepository repo;

    public DayPresenterImpl(DayMealView view, MealsRepository repo){
        this._view=view;
        this.repo=repo;
    }
    @Override
    public LiveData<List<DayMeal>> getMeals() {
        return repo.getDayMealList();
    }

    @Override
    public LiveData<List<DayMeal>> getMealsByDate(String selectedDate) {
        return repo.getDayMealListByDate(selectedDate);
    }


    @Override
    public void deleteDayMeal(DayMeal meal) {
        mealsLocalDataSource.deleteDayMeal(meal);
    }
}
