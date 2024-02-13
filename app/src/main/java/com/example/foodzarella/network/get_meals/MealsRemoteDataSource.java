package com.example.foodzarella.network.get_meals;

public interface MealsRemoteDataSource {
    void makeNetworkCall(NetworkCallback networkCallback,String category);
}
