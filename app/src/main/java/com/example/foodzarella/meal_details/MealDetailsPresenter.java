package com.example.foodzarella.meal_details;

import com.example.foodzarella.network.MealApiService;
import com.example.foodzarella.network.MealDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetailsPresenter {
    private MealApiService apiService;
    private MealDetailsView view;

    public MealDetailsPresenter(MealApiService apiService, MealDetailsView view) {
        this.apiService = apiService;
        this.view = view;
    }

    public void getMealDetails(String mealId) {
        Call<MealDetailsResponse> call = apiService.getMealDetails(mealId);
        call.enqueue(new Callback<MealDetailsResponse>() {
            @Override
            public void onResponse(Call<MealDetailsResponse> call, Response<MealDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MealDetailsResponse mealDetailsResponse = response.body();
                    List<MealDetails> meals = mealDetailsResponse.getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        MealDetails mealDetails = meals.get(0);
                        view.displayMealDetails(mealDetails);
                    } else {

                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<MealDetailsResponse> call, Throwable t) {
                // Handle network errors or API call failure
            }
        });
    }
}
