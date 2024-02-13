package com.example.foodzarella.allmeals.presenter;


import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepository;
import com.example.foodzarella.network.get_meals.NetworkCallback;

import java.util.List;

public class AllMealsPresenterImpl implements AllMealsPresenter, NetworkCallback {
    private AllMealView view;
    private MealsRepository repo;

    private String category;
public AllMealsPresenterImpl(AllMealView view, MealsRepository repo,String category){
    this.view=view;
    this.repo=repo;
    this.category=category;
}
    @Override
    public void getMeals() {
    repo.getAllMeals(this,category);
    }

    @Override
    public void getMealsByCategory() {
        repo.getAllMeals(this,category);
    }

    @Override
    public void addToFav(Meal meal) {
        repo.insert(meal);
    }

    @Override
    public void onSuccessResult(List<Meal> mealList) {
        view.showData(mealList);
    }

    @Override
    public void onFailureResult(String errorMsg) {
        view.showErrMsg(errorMsg);
    }
}
