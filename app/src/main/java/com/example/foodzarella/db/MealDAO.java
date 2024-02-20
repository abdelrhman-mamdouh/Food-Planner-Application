package com.example.foodzarella.db;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodzarella.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface MealDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Meal meal);

    @Update
    void update(Meal meal);

    @Delete
    void delete(Meal meal);

    @Query("SELECT * FROM meal_table")
    Flowable<List<Meal>> getAllMeals();
    @Query("SELECT EXISTS (SELECT 1 FROM meal_table WHERE meal_id = :mealId)")
    Flowable<Boolean> isMealExists(String mealId);

}
