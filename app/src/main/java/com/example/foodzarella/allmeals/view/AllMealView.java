package com.example.foodzarella.allmeals.view;

import com.example.foodzarella.model.Meal;
import com.example.mvptask.model.Product;

import java.util.List;

public interface AllMealView {
    public void showData(List<Meal> products);
    public void showErrMsg(String error);
}
