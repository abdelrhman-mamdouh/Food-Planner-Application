package com.example.foodzarella.network.getMeals;

public interface MealsRemoteDataSource {
    void makeNetworkCall(NetworkCallback networkCallback,String category);
}
