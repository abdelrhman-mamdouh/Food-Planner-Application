package com.example.foodzarella.allmeals.presenter;


import androidx.annotation.NonNull;

import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealResponse;
import com.example.foodzarella.model.MealsRepository;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.get_meals.MealQuery;
import com.example.foodzarella.network.get_meals.NetworkCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllMealsPresenterImpl implements AllMealsPresenter, NetworkCallback {
    private AllMealView view;
    private MealsRepository repo;

    private String searchBy;
public AllMealsPresenterImpl(AllMealView view, MealsRepository repo,String searchBy){
    this.view=view;
    this.repo=repo;
    this.searchBy =searchBy;
}
    @Override
    public void getMeals() {

        Call<MealResponse> call =ApiClient.getClient().create(MealQuery.class).getMeals("chicken_breast");
        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> mealList = response.body().getMeals();
                    view.showData(mealList);
                }
            }
            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable throwable) {
                view.showErrMsg("Failed to fetch meals");
            }
        });
    }

    @Override
    public void getMealsByCategory() {
        Call<MealResponse> callCategory = ApiClient.getClient().create(MealQuery.class).getMealsByCategory(searchBy);
        callCategory.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> mealList = response.body().getMeals();
                    view.showData(mealList);
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable throwable) {
                view.showErrMsg("Failed to fetch categories");
            }
        });
    }

    @Override
    public void getMealsByCountry() {
        Call<MealResponse> callCountry = ApiClient.getClient().create(MealQuery.class).getMealsByCountry(searchBy);
        callCountry.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meal> mealList = response.body().getMeals();
                    view.showData(mealList);
                }
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable throwable) {
                view.showErrMsg("Failed to fetch categories");
            }
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
