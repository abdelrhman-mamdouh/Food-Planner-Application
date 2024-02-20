package com.example.foodzarella.categories.presenter;

import com.example.foodzarella.categories.model.Category;
import com.example.foodzarella.categories.view.CategoryView;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.getMeals.MealQuery;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;

public class CategoryPresenterImpl implements CategoryPresenter {
    private CategoryView view;
    private MealQuery categoryService;

    public CategoryPresenterImpl(CategoryView view) {
        this.view = view;
        this.categoryService = ApiClient.getClient().create(MealQuery.class);
    }

    @Override
    public void getCategories() {
        categoryService.getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(categoryResponse -> {
                    List<Category> categories = categoryResponse.getCategories();
                    view.showCategories(categories);
                }, throwable -> {
                    view.showError(throwable.getMessage());
                });
    }
}