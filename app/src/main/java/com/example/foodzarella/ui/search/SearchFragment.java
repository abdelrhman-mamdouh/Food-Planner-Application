package com.example.foodzarella.ui.search;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenter;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenterImpl;
import com.example.foodzarella.allmeals.view.AllMealView;
import com.example.foodzarella.allmeals.view.MealAdapter;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;

public class SearchFragment extends Fragment implements AllMealView {
    private MealAdapter mealAdapter;
    private AllMealsPresenter allMealsPresenter;
    String[] categories = {"Beef", "Chicken", "Dessert", "Lamb", "Miscellaneous", "Pasta", "Pork", "Seafood", "Side", "Starter", "Vegan", "Vegetarian", "Breakfast", "Goat"};
    String[] countries = {"American", "British", "Canadian", "Chinese", "Croatian", "Dutch", "Egyptian", "Filipino", "French", "Greek", "Indian", "Irish", "Italian", "Jamaican", "Japanese", "Kenyan", "Malaysian", "Mexican", "Moroccan", "Polish", "Portuguese", "Russian", "Spanish", "Thai", "Tunisian", "Turkish", "Unknown", "Vietnamese"};

    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;
    RecyclerView recyclerView;
    String countryName;
    private ChipGroup chipGroup;
    private ProgressBar loader;
    private EditText searchEditText;
    private TextView textViewFilerName;
    Chip selectedChip;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewCategory);
        searchEditText = view.findViewById(R.id.search_food);
        loader = view.findViewById(R.id.loader);
        textViewFilerName = view.findViewById(R.id.tv_filter);
        autoCompleteTxt = view.findViewById(R.id.auto_complete_txt);
        chipGroup = view.findViewById(R.id.chipGroup);
        initializeRecyclerViewCategory(recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                selectedChip = view.findViewById(checkedId);
                switch (selectedChip.getText().toString()) {
                    case "Country":
                        changeFilter("Country", "Select Country", countries);
                        break;
                    case "Ingredient":
                        changeFilter("Ingredient", "Select Ingredient", countries);
                        break;
                    case "Category":
                        changeFilter("Category", "Select Category", categories);
                        break;
                }
            }
        });

        chipGroup.check(R.id.chipCategory);
        autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showLoader();
                String searchBy = parent.getItemAtPosition(position).toString();
                if (selectedChip != null) {
                    if (selectedChip.getText().toString().equals("Category")) {
                        searchMealsByCategory(searchBy);
                    } else if (selectedChip.getText().toString().equals("Country")) {
                        searchMealsByCountry(searchBy);
                    }
                }
            }
        });
    }

    private void changeFilter(String filterName, String selectName, String[] listNames) {
        textViewFilerName.setText(filterName);
        autoCompleteTxt.setText(selectName);
        adapterItems = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listNames);
        autoCompleteTxt.setAdapter(adapterItems);
    }

    private void searchMealsByCategory(String category) {
        allMealsPresenter = new AllMealsPresenterImpl(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireContext())), category);
        recyclerView.setAdapter(mealAdapter);
        allMealsPresenter.getMealsByCategory();
    }

    private void searchMealsByCountry(String category) {
        allMealsPresenter = new AllMealsPresenterImpl(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(requireContext())), category);
        recyclerView.setAdapter(mealAdapter);
        allMealsPresenter.getMealsByCountry();
    }

    private void initializeRecyclerViewCategory(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mealAdapter = new MealAdapter(new ArrayList<>(), getContext(), MealsLocalDataSourceImpl.getInstance(requireContext()), MealsRemoteSourceDataImpl.getInstance());
    }

    @Override
    public void showData(List<Meal> meals) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mealAdapter.setList(meals);
                mealAdapter.notifyDataSetChanged();
                RxTextView.textChanges(searchEditText)
                        .debounce(100, TimeUnit.MILLISECONDS)
                        .map(text -> text.toString())
                        .subscribe(text -> {
                            filterType(text, meals);
                        });
                hideLoader();
            }
        }, 2000);
    }

    @Override
    public void showErrMsg(String error) {
        new AlertDialog.Builder(requireContext())
                .setMessage(error)
                .setTitle("An Error Occurred")
                .show();
        hideLoader();
    }

    private void showLoader() {
        loader.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loader.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoCompleteTxt.setText("");
    }

    private void filterType(String text, List<Meal> mealList) {
        Observable.fromIterable(mealList)
                .filter(name -> name.getStrMeal().toLowerCase().contains(text.toLowerCase()) || text.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(meals -> {
                    mealAdapter.setList(meals);
                    mealAdapter.notifyDataSetChanged();
                });
    }
}