package com.example.foodzarella.categories.view;

import com.example.foodzarella.categories.model.Category;

import java.util.List;

public interface CategoryView {
    void showCategories(List<Category> categories);
    void showError(String message);
}