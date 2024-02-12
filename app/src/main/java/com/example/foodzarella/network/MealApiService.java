package com.example.foodzarella.network;

import com.example.foodzarella.meal_details.MealDetails;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MealApiService {
    @GET("lookup.php")
    Call<MealDetailsResponse> getMealDetails(@Query("i") String mealId);
}