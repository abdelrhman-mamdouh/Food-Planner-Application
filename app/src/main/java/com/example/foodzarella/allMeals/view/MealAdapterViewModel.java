package com.example.foodzarella.allMeals.view;

import androidx.lifecycle.ViewModel;

public class MealAdapterViewModel extends ViewModel {
    private boolean isFavorite = false;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}