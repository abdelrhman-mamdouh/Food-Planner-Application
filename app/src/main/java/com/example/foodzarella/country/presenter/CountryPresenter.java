package com.example.foodzarella.country.presenter;

import com.example.foodzarella.country.view.CountryView;
import com.example.foodzarella.country.model.MealCountriesResponse;
import com.example.foodzarella.country.model.MealCountry;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.get_meals.MealQuery;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
        categoryService.getCountries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealCountriesResponse -> {
                    List<MealCountry> countries = mealCountriesResponse.getCountries();
                    view.showCountries(countries);
                }, throwable -> {
                    view.showError(throwable.getMessage());
                });
    }
}
