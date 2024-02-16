package com.example.foodzarella.country.view;

import com.example.foodzarella.country.model.MealCountry;

import java.util.List;

public interface CountryView {
    void showCountries(List<MealCountry> countries);
    void showError(String message);


}
