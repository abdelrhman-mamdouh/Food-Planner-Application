package com.example.foodzarella.network.get_meals;



import com.example.foodzarella.categories.model.CategoryResponse;
import com.example.foodzarella.country.model.MealCountriesResponse;
import com.example.foodzarella.model.MealResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealQuery {
    @GET("filter.php")
    Single<MealResponse> getMeals(@Query("i") String ingredient);
    @GET("filter.php")
    Single<MealResponse> getMealsByCategory(@Query("c") String category);
    @GET("filter.php")
    Single<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("categories.php")
    Single<CategoryResponse> getCategories();
    @GET("list.php?a=list")
    Single<MealCountriesResponse> getCountries();

}
