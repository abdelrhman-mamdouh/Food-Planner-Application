package com.example.foodzarella.ui.search;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.allmeals.presenter.AllMealsPresenter;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenterImpl;
import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.allmeals.view.MealAdapter;
import com.example.foodzarella.databinding.FragmentSearchBinding;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;

import java.util.List;

public class SearchFragment extends Fragment implements AllMealView {
    private FragmentSearchBinding binding;
    private MealAdapter mealAdapter;
    private AllMealsPresenter allMealsPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle byCategory = getArguments();
        String categoryName="Seafood";
        if (byCategory != null) {
             categoryName = byCategory.getString("categoryName");
        }
        RecyclerView recyclerView = binding.recyclerViewCategory;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        allMealsPresenter = new AllMealsPresenterImpl(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireContext())),categoryName);
        mealAdapter = new MealAdapter(null, requireContext(), MealsLocalDataSourceImpl.getInstance(requireContext()), MealsRemoteSourceDataImpl.getInstance());
        recyclerView.setAdapter(mealAdapter);
        allMealsPresenter.getMealsByCategory();


        Bundle byCountry = getArguments();
        String countryName="Egyptian";
        if (byCountry != null) {
            countryName = byCountry.getString("countryName");
        }
        RecyclerView recyclerViewCountry = binding.recyclerViewCountry;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManagerCountry = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCountry.setLayoutManager(linearLayoutManagerCountry);
        allMealsPresenter = new AllMealsPresenterImpl(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireContext())),countryName);
        mealAdapter = new MealAdapter(null, requireContext(), MealsLocalDataSourceImpl.getInstance(requireContext()), MealsRemoteSourceDataImpl.getInstance());
        recyclerViewCountry.setAdapter(mealAdapter);
        allMealsPresenter.getMealsByCategory();

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

}
