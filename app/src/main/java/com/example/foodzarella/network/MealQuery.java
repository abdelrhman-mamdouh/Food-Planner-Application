package com.example.foodzarella.network;



import com.example.foodzarella.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MealQuery {
    @GET("chicken_breast")
    Call<MealResponse> getMeals();
}
