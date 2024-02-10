package com.example.foodzarella.allmeals.view;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodzarella.R;
import com.example.foodzarella.allmeals.presenter.AllMealsPresenter;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.MealsRemoteSourceDataImpl;

import java.util.List;

public class AllMealsActivity extends AppCompatActivity implements AllMealView {
    private MealAdapter mealAdapter;
    private AllMealsPresenter allMealsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_all_products);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        allMealsPresenter = new AllMealsPresenter(this, MealsRepositoryImol.getInstance(MealsRemoteSourceDataImpl.getInstance(), MealsLocalDataSourceImpl.getInstance(this)));

        mealAdapter = new MealAdapter(null, this, MealsLocalDataSourceImpl.getInstance(this), MealsRemoteSourceDataImpl.getInstance());

        recyclerView.setAdapter(mealAdapter);
        allMealsPresenter.getMeals();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error).setTitle("An Error Occurred");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}