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

import com.example.foodzarella.allmeals.presenter.AllMealsPresenter;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenterImpl;
import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.allmeals.view.MealAdapter;
import com.example.foodzarella.categorys.model.Category;
import com.example.foodzarella.categorys.presenter.CategoryPresenter;
import com.example.foodzarella.categorys.view.CategoryView;
import com.example.foodzarella.categorys.view.GategoryAdapter;
import com.example.foodzarella.country.CountryAdapter;
import com.example.foodzarella.country.CountryPresenter;
import com.example.foodzarella.country.CountryView;
import com.example.foodzarella.country.MealCountry;
import com.example.foodzarella.databinding.FragmentHomeBinding;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements AllMealView, CategoryView, CountryView {

    private FragmentHomeBinding binding;
    private MealAdapter mealAdapter;

    private GategoryAdapter gategoryAdapter;

    private CategoryPresenter categoryPresenter;
    private AllMealsPresenter allMealsPresenter;

    private CountryPresenter countryPresenter;
    private CountryAdapter countryAdapter;
    private Fragment fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        allMealsPresenter = new AllMealsPresenterImpl(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireContext())),"chicken_breast");

        mealAdapter = new MealAdapter(null, requireContext(), MealsLocalDataSourceImpl.getInstance(requireContext()), MealsRemoteSourceDataImpl.getInstance());

        recyclerView.setAdapter(mealAdapter);
        allMealsPresenter.getMeals();

        fragment=this;


        RecyclerView recyclerViewCategory = binding.recyclerView2;
        GridLayoutManager gridLayoutManager= new GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerViewCategory.setLayoutManager(gridLayoutManager);
        recyclerViewCategory.setHasFixedSize(true);
        categoryPresenter = new CategoryPresenter(this);
        gategoryAdapter = new GategoryAdapter(new ArrayList<>(), requireContext(),fragment);

        recyclerViewCategory.setAdapter(gategoryAdapter);
        categoryPresenter.getCategories();


        RecyclerView recyclerViewCountry = binding.recyclerView3;
        GridLayoutManager gridLayoutManagerCountry= new GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false);
        recyclerViewCountry.setLayoutManager(gridLayoutManagerCountry);
        recyclerViewCountry.setHasFixedSize(true);
        countryPresenter = new CountryPresenter(this);
        countryAdapter = new CountryAdapter(new ArrayList<>(), requireContext(),fragment);
        recyclerViewCountry.setAdapter(countryAdapter);
        countryPresenter.getCountries();

    }

    @Override
    public void showData(List<Meal> meals) {
        if (mealAdapter != null) {
            mealAdapter.setList(meals);
            mealAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrMsg(String error) {
        new AlertDialog.Builder(requireContext())
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

    }

    @Override
    public void showCountries(List<MealCountry> countries) {
        countryAdapter.setList(countries);
        countryAdapter.notifyDataSetChanged();
    }

}
