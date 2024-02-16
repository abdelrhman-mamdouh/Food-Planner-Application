package com.example.foodzarella.network.get_meals;



import com.example.foodzarella.categorys.model.CategoryResponse;
import com.example.foodzarella.country.model.MealCountriesResponse;
import com.example.foodzarella.model.MealResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealQuery {
    @GET("filter.php")
    Call<MealResponse> getMeals(@Query("i") String ingredient);
    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);
    @GET("filter.php")
    Call<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("categories.php")
    Call<CategoryResponse> getCategories();
    @GET("list.php?a=list")
    Call<MealCountriesResponse> getCountries();

    @GET("search.php")
    Call<MealResponse> searchMeals(@Query("s") String query);
}
