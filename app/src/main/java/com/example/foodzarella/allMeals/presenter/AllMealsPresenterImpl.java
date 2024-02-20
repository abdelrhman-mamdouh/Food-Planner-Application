package com.example.foodzarella.allMeals.presenter;


import com.example.foodzarella.allMeals.view.AllMealView;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepository;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.getMeals.MealQuery;
import com.example.foodzarella.network.getMeals.NetworkCallback;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllMealsPresenterImpl implements AllMealsPresenter, NetworkCallback {
    private AllMealView view;
    private MealsRepository repo;
    private final MealQuery mealQuery;
    private String searchBy;
public AllMealsPresenterImpl(AllMealView view, MealsRepository repo,String searchBy){
    this.view=view;
    this.repo=repo;
    this.searchBy =searchBy;
    this.mealQuery = ApiClient.getClient().create(MealQuery.class);
}
    @Override
    public void getMeals() {
        mealQuery.getMeals("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    List<Meal> mealList = mealResponse.getMeals();
                    view.showData(mealList);
                }, throwable -> {
                    view.showErrMsg("Failed to fetch meals");
                });
    }

    @Override
    public void getMealsByCountry() {
        mealQuery.getMealsByCountry(searchBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    List<Meal> mealList = mealResponse.getMeals();
                    view.showData(mealList);
                }, throwable -> {
                    view.showErrMsg("Failed to fetch categories");
                });
    }
    @Override
    public void getMealsByCategory() {
        mealQuery.getMealsByCategory(searchBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    List<Meal> mealList = mealResponse.getMeals();
                    view.showData(mealList);
                }, throwable -> {
                    view.showErrMsg("Failed to fetch categories");
                });
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
