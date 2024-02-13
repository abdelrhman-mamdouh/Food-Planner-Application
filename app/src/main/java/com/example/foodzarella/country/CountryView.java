package com.example.foodzarella.country;

import com.example.foodzarella.categorys.model.Category;

import java.util.List;

public interface CountryView {
    void showCountries(List<MealCountry> countries);
    void showError(String message);


}
