package com.example.foodzarella.ui.favorites;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodzarella.databinding.FragmentFavoritesBinding;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.favmeals.presenter.FavPresenterImpl;
import com.example.foodzarella.favmeals.view.FacMealView;
import com.example.foodzarella.favmeals.view.FavAdapter;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;

import java.util.List;

public class FavoritesFragment extends Fragment implements FacMealView{
    private FragmentFavoritesBinding binding;
    private RecyclerView recyclerView;
    private FavAdapter favAdapter;
    private MealsLocalDataSource localDataSource;
    private MealsRemoteDataSource remoteDataSource;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.recyclerViewFav;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        localDataSource = MealsLocalDataSourceImpl.getInstance(requireContext());
        remoteDataSource = MealsRemoteSourceDataImpl.getInstance();

        favAdapter = new FavAdapter(null, requireContext(), localDataSource, remoteDataSource);
        recyclerView.setAdapter(favAdapter);

        FavPresenterImpl favPresenter = new FavPresenterImpl(this, MealsRepositoryImol.getInstance(remoteDataSource, localDataSource));
        showData(favPresenter.getMeals());


    }

    @Override
    public void showData(LiveData<List<Meal>> meals) {
        meals.observe(getViewLifecycleOwner(), new Observer<List<Meal>>() {
            @Override
            public void onChanged(List<Meal> mealList) {
                favAdapter.setList(mealList);
                favAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showErrMsg(String error) {
        new AlertDialog.Builder(requireContext())
                .setMessage(error)
                .setTitle("An Error Occurred")
                .show();
    }
}
