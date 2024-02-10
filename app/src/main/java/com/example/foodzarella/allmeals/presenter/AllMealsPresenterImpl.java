package com.example.foodzarella.allmeals.presenter;


import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepository;
import com.example.foodzarella.network.NetworkCallback;

import java.util.List;

public class AllMealsPresenterImpl implements AllMealsPresenter, NetworkCallback {
    private AllMealView view;
    private MealsRepository repo;
public AllMealsPresenterImpl(AllMealView view, MealsRepository repo){
    this.view=view;
    this.repo=repo;
}
    @Override
    public void getMeals() {
    repo.getAllMeals(this);
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
