package com.example.foodzarella.favMeals.view.favorites;

import static com.example.foodzarella.SnackbarUtils.showTopSnackbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.airbnb.lottie.LottieAnimationView;
import com.example.foodzarella.R;
import com.example.foodzarella.databinding.FragmentFavoritesBinding;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.favMeals.presenter.FavPresenterImpl;
import com.example.foodzarella.favMeals.view.FavMealView;
import com.example.foodzarella.favMeals.view.FavAdapter;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.getMeals.MealsRemoteDataSource;
import com.example.foodzarella.network.getMeals.MealsRemoteSourceDataImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FavoritesFragment extends Fragment implements FavMealView {
    private FragmentFavoritesBinding binding;
    private RecyclerView recyclerView;
    private FavAdapter favAdapter;
    private MealsLocalDataSource localDataSource;
    private MealsRemoteDataSource remoteDataSource;
    private EditText searchEditText;
    private ProgressBar loader;

    LottieAnimationView backgroundAnimationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEditText = view.findViewById(R.id.search_food);
        backgroundAnimationView = view.findViewById(R.id.loaderss);
        recyclerView = binding.recyclerViewFav;
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        localDataSource = MealsLocalDataSourceImpl.getInstance(requireContext());
        remoteDataSource = MealsRemoteSourceDataImpl.getInstance();

        favAdapter = new FavAdapter(null, requireContext(), localDataSource, remoteDataSource);
        recyclerView.setAdapter(favAdapter);


        if (isNetworkAvailable()) {
            fetchRemoteData();
        } else {
            fetchLocalData();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void fetchRemoteData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            getFavoriteMealsFromFirestore(userId, new OnMealsLoadedListener() {
                @Override
                public void onMealsLoaded(List<Meal> meals) {
                    favAdapter.setList(meals);
                    favAdapter.notifyDataSetChanged();
                    RxTextView.textChanges(searchEditText)
                            .debounce(100, TimeUnit.MILLISECONDS)
                            .map(text -> text.toString())
                            .subscribe(text -> {
                                filterType(text, meals);
                            });
                    if (isDatasetEmpty()) {
                        showLoader();
                    } else {
                        hideLoader();
                        favAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    showErrMsg("Failed to fetch data from Firestore");
                }
            });
        }
    }

    private void fetchLocalData() {
        FavPresenterImpl favPresenter = new FavPresenterImpl(this, MealsRepositoryImol.getInstance(remoteDataSource, localDataSource));
        showData(favPresenter.getMeals());
    }

    @Override
    public void showData(Flowable<List<Meal>> mealsFlowable) {
        mealsFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<List<Meal>>() {
                    @Override
                    public void onNext(List<Meal> mealList) {
                        favAdapter.setList(mealList);
                        favAdapter.notifyDataSetChanged();
                        RxTextView.textChanges(searchEditText)
                                .debounce(100, TimeUnit.MILLISECONDS)
                                .map(text -> text.toString())
                                .subscribe(text -> {
                                    filterType(text, mealList);
                                });
                        if (isDatasetEmpty()) {
                            showLoader();
                        } else {
                            hideLoader();
                            favAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onError(Throwable t) {
                        showTopSnackbar(getView(), "Can not be retrieved", "red");
                    }
                    @Override
                    public void onComplete() {
                        showTopSnackbar(getView(), "Done", "green");
                    }
                });
    }

    @Override
    public void showErrMsg(String error) {
        showTopSnackbar(getView(), "No internet connection", "red");
    }

    private void filterType(String text, List<Meal> mealList) {
        Observable.fromIterable(mealList)
                .filter(name -> name.getStrMeal().toLowerCase().contains(text.toLowerCase()) || text.isEmpty())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(filteredMeals -> {
                    favAdapter.setList(filteredMeals); // Set the filtered list here
                    favAdapter.notifyDataSetChanged();
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        favAdapter.notifyDataSetChanged();
        if (isDatasetEmpty()) {
            showLoader();
        } else {
            hideLoader();
            favAdapter.notifyDataSetChanged();
        }
    }
    private void showLoader() {
        backgroundAnimationView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        backgroundAnimationView.setVisibility(View.GONE);
    }
    private boolean isDatasetEmpty() {
        if (favAdapter != null) {
            return favAdapter.getItemCount() == 0;
        }
        return true;
    }
    public void getFavoriteMealsFromFirestore(String userId, OnMealsLoadedListener listener) {
        CollectionReference favoriteMealsRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("favoriteMeals");

        favoriteMealsRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Meal> meals = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        meals.add(document.toObject(Meal.class));
                    }
                    listener.onMealsLoaded(meals);
                })
                .addOnFailureListener(listener::onFailure);
    }

    public interface OnMealsLoadedListener {
        void onMealsLoaded(List<Meal> meals);

        void onFailure(Exception e);
    }
}
