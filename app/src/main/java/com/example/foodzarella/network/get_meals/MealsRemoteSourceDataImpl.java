package com.example.foodzarella.network.get_meals;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealResponse;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteSourceDataImpl implements MealsRemoteDataSource {
    private static final String TAG = "AllMealsActivity";
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit retrofit;
    private static MealsRemoteDataSource instance;
    private final MealQuery mealService;

    public static MealsRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new MealsRemoteSourceDataImpl();
        }
        return instance;
    }

    private MealsRemoteSourceDataImpl() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        mealService = retrofit.create(MealQuery.class);
    }

    @Override
    public void makeNetworkCall(NetworkCallback networkCallback,String searchBy) {
     
        mealService.getMeals(searchBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    Log.i(TAG, "onSuccessResult: " + mealResponse);
                    networkCallback.onSuccessResult(mealResponse.getMeals());
                }, throwable -> {
                    Log.e(TAG, "onFailureResult: " + throwable.getMessage());
                    networkCallback.onFailureResult(throwable.getMessage());
                });

        mealService.getMealsByCategory(searchBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    Log.i(TAG, "onSuccessResult (Category): " + mealResponse);
                    networkCallback.onSuccessResult(mealResponse.getMeals());
                }, throwable -> {
                    Log.e(TAG, "onFailureResult (Category): " + throwable.getMessage());
                    networkCallback.onFailureResult(throwable.getMessage());
                });

        mealService.getMealsByCountry(searchBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mealResponse -> {
                    Log.i(TAG, "onSuccessResult (Country): " + mealResponse);
                    networkCallback.onSuccessResult(mealResponse.getMeals());
                }, throwable -> {
                    Log.e(TAG, "onFailureResult (Country): " + throwable.getMessage());
                    networkCallback.onFailureResult(throwable.getMessage());
                });
    }
}