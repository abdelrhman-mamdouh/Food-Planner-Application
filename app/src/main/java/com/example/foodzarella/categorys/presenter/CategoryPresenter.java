package com.example.foodzarella.categorys.presenter;

import com.example.foodzarella.categorys.model.Category;
import com.example.foodzarella.categorys.model.CategoryResponse;
import com.example.foodzarella.categorys.view.CategoryView;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.get_meals.MealQuery;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class CategoryPresenter {
    private CategoryView view;
    private MealQuery categoryService;

    public CategoryPresenter(CategoryView view) {
        this.view = view;
        this.categoryService = ApiClient.getClient().create(MealQuery.class);
    }

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
