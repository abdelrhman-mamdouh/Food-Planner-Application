package com.example.foodzarella.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenter;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenterImpl;
import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.allmeals.view.MealAdapter;
import com.example.foodzarella.categories.model.Category;
import com.example.foodzarella.categories.presenter.CategoryPresenterImpl;
import com.example.foodzarella.categories.view.CategoryView;
import com.example.foodzarella.categories.view.GategoryAdapter;
import com.example.foodzarella.country.view.CountryAdapter;
import com.example.foodzarella.country.presenter.CountryPresenter;
import com.example.foodzarella.country.view.CountryView;
import com.example.foodzarella.country.model.MealCountry;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;
import com.example.foodzarella.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AllMealView, CategoryView, CountryView {

    private MealAdapter mealAdapter;
    private GategoryAdapter gategoryAdapter;
    private CategoryPresenterImpl categoryPresenter;
    private AllMealsPresenter allMealsPresenter;
    private CountryPresenter countryPresenter;
    private CountryAdapter countryAdapter;
    LottieAnimationView backgroundAnimationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backgroundAnimationView = view.findViewById(R.id.loaderss);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        allMealsPresenter = new AllMealsPresenterImpl(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(getContext())), "chicken_breast");

        mealAdapter = new MealAdapter(new ArrayList<>(), getContext(), MealsLocalDataSourceImpl.getInstance(getContext()), MealsRemoteSourceDataImpl.getInstance());

        recyclerView.setAdapter(mealAdapter);
        allMealsPresenter.getMeals();



        RecyclerView recyclerViewCategory = view.findViewById(R.id.recyclerView2);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        recyclerViewCategory.setHasFixedSize(true);
        categoryPresenter = new CategoryPresenterImpl(this);
        gategoryAdapter = new GategoryAdapter(new ArrayList<>(), getContext(), this);

        recyclerViewCategory.setAdapter(gategoryAdapter);
        categoryPresenter.getCategories();

        RecyclerView recyclerViewCountry = view.findViewById(R.id.recyclerView3);
        GridLayoutManager gridLayoutManagerCountry = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerViewCountry.setLayoutManager(gridLayoutManagerCountry);
        recyclerViewCountry.setHasFixedSize(true);
        countryPresenter = new CountryPresenter(this);
        countryAdapter = new CountryAdapter(new ArrayList<>(), getContext(), this);
        recyclerViewCountry.setAdapter(countryAdapter);
        countryPresenter.getCountries();
    }

    @Override
    public void showData(List<Meal> meals) {
        mealAdapter.setList(meals);
        mealAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrMsg(String error) {
        new AlertDialog.Builder(getContext())
                .setMessage(error)
                .setTitle("An Error Occurred")
                .show();
    }

    @Override
    public void showCategories(List<Category> categories) {
        gategoryAdapter.setList(categories);
        gategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        // Handle error if needed
    }

    @Override
    public void showCountries(List<MealCountry> countries) {
        countryAdapter.setList(countries);
        countryAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        mealAdapter.notifyDataSetChanged();
        if (isDatasetEmpty()) {
            showLoader();
        } else {
            hideLoader();
            mealAdapter.notifyDataSetChanged();
        }
    }
    private boolean isDatasetEmpty() {

        if (mealAdapter != null) {
            return mealAdapter.getItemCount() == 0;
        }
        return true;
    }


    private void showLoader() {
        backgroundAnimationView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        backgroundAnimationView.setVisibility(View.GONE);
    }

}
