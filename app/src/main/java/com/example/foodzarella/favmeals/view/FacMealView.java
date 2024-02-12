package com.example.foodzarella.favmeals.view;

import androidx.lifecycle.LiveData;

import com.example.foodzarella.model.Meal;


import java.util.List;

public interface FacMealView {
    public void showData(LiveData <List<Meal>> products);
    public void showErrMsg(String error);
}
