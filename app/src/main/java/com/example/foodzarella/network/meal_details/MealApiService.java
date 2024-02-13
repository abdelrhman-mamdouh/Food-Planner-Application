package com.example.foodzarella.network.meal_details;

import com.example.foodzarella.network.meal_details.MealDetailsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface MealApiService {
    @GET("lookup.php")
    Call<MealDetailsResponse> getMealDetails(@Query("i") String mealId);
}