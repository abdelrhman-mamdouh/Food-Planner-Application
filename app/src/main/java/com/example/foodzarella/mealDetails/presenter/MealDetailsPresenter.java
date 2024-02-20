package com.example.foodzarella.mealDetails.presenter;

import com.example.foodzarella.mealDetails.model.MealDetails;
import com.example.foodzarella.mealDetails.view.MealDetailsView;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.getMeals.MealQuery;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealDetailsPresenter {
    private MealQuery mealQuery;
    private MealDetailsView view;

    public MealDetailsPresenter(MealQuery mealQuery, MealDetailsView view) {
        this.mealQuery = ApiClient.getClient().create(MealQuery.class);
        this.view = view;
    }

    public void getMealDetails(String mealId) {
        mealQuery.getMealDetails(mealId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealDetailsResponse -> {
                    List<MealDetails> meals = mealDetailsResponse.getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        MealDetails mealDetails = meals.get(0);
                        view.displayMealDetails(mealDetails);
                    } else {
                        // Handle empty response
                    }
                }, throwable -> {
                    // Handle network errors or API call failure
                });
    }
}
