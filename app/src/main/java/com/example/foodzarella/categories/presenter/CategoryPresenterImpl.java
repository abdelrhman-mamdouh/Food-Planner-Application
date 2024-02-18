package com.example.foodzarella.categories.presenter;

import com.example.foodzarella.categories.model.Category;
import com.example.foodzarella.categories.model.CategoryResponse;
import com.example.foodzarella.categories.view.CategoryView;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.get_meals.MealQuery;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        categoryService.getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body().getCategories();
                    view.showCategories(categories);
                } else {
                    view.showError("Failed to fetch categories");
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                view.showError(t.getMessage());
            }
        });
    }
}
