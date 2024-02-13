package com.example.foodzarella.categorys.view;

import com.example.foodzarella.categorys.model.Category;

import java.util.List;

public interface CategoryView {
    void showCategories(List<Category> categories);
    void showError(String message);
}