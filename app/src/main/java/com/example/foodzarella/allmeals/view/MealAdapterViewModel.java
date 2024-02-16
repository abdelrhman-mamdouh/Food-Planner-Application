package com.example.foodzarella.allmeals.view;

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