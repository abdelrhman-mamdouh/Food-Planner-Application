package com.example.foodzarella.allMeals.view;

import com.example.foodzarella.model.Meal;


import java.util.List;

public interface AllMealView {
    public void showData(List<Meal> meals);
    public void showErrMsg(String error);

}
