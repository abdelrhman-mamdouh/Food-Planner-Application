package com.example.foodzarella.favmeals.presenter;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.favmeals.view.FavMealView;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepository;

import java.util.List;

public class FavPresenterImpl implements FavMealsPresenter {
    private FavMealView _view;
    private MealsLocalDataSource mealsLocalDataSource;
    private MealsRepository repo;

    public FavPresenterImpl(FavMealView view, MealsRepository repo){
        this._view=view;
        this.repo=repo;
    }
    @Override
    public LiveData<List<Meal>> getMeals() {
        return repo.getMealList();
    }
    @Override
    public void deleteFromFav(Meal meal) {
        mealsLocalDataSource.delete(meal);
    }
}
