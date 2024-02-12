package com.example.foodzarella.network;



import com.example.foodzarella.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealQuery {
    @GET("filter.php")
    Call<MealResponse> getMeals(@Query("i") String ingredient);
}
