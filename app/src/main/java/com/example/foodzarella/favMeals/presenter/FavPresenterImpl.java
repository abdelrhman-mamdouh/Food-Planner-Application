package com.example.foodzarella.favMeals.presenter;

import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.favMeals.view.FavMealView;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepository;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class FavPresenterImpl implements FavMealsPresenter {
    private FavMealView _view;
    private MealsLocalDataSource mealsLocalDataSource;
    private MealsRepository repo;

    public FavPresenterImpl(FavMealView view, MealsRepository repo){
        this._view=view;
        this.repo=repo;
    }
    @Override
    public Flowable<List<Meal>> getMeals() {
        return repo.getMealList();
    }
    @Override
    public void deleteFromFav(Meal meal) {
        mealsLocalDataSource.delete(meal);
    }
}
