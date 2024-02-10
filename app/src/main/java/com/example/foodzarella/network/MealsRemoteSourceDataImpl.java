package com.example.foodzarella.network;

import android.util.Log;

import androidx.annotation.NonNull;


import com.example.foodzarella.model.MealResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteSourceDataImpl implements MealsRemoteDataSource {
    private static final String TAG = "AllProductsActivity";
    private static final String BASE_URL = "www.themealdb.com/api/json/v1/1/filter.php?i=";
    private static Retrofit retrofit;

    private static MealsRemoteDataSource retrofitClass= null;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static MealsRemoteDataSource getInstance(){
        if(retrofitClass==null){
            retrofitClass=new MealsRemoteSourceDataImpl();
        }
        return retrofitClass;
    }
    @Override
    public void makeNetworkCall(NetworkCallback networkCallback){
        MealQuery mealService = MealsRemoteSourceDataImpl.getRetrofitInstance().create(MealQuery.class);
        Call<MealResponse> call = mealService.getMeals();

        call.enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(@NonNull Call<MealResponse> call, @NonNull Response<MealResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.i(TAG, "onResponse: CallBack"+response.raw()+response.body());
                    networkCallback.onSuccessResult(response.body().getMeals());
                }
            }


            @Override
            public void onFailure(@NonNull Call<MealResponse> call, @NonNull Throwable throwable) {
                Log.i(TAG, "onFailure: CallBack");
                networkCallback.onFailureResult(throwable.getMessage());
                throwable.printStackTrace();
            }
        });
    }
}