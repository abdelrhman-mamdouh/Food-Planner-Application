package com.example.foodzarella.country.presenter;

import com.example.foodzarella.country.view.CountryView;
import com.example.foodzarella.country.model.MealCountriesResponse;
import com.example.foodzarella.country.model.MealCountry;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.get_meals.MealQuery;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryPresenter {
    private CountryView view;
    private MealQuery categoryService;

    public CountryPresenter(CountryView view) {
        this.view = view;
        this.categoryService = ApiClient.getClient().create(MealQuery.class);
    }

    public void getCountries() {
        categoryService.getCountries().enqueue(new Callback<MealCountriesResponse>() {
            @Override
            public void onResponse(Call<MealCountriesResponse> call, Response<MealCountriesResponse> response) {
                if (response.isSuccessful()) {
                    List<MealCountry> countries = response.body().getCountries();
                    view.showCountries(countries);
                } else {
                    view.showError("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(Call<MealCountriesResponse> call, Throwable t) {
                view.showError(t.getMessage());
            }
        });
    }

}
