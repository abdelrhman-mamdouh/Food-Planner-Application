package com.example.foodzarella.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface DayMealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DayMeal meal);

    @Update
    void update(DayMeal meal);

    @Delete
    void delete(DayMeal meal);
    @Query("SELECT * FROM day_meal_table")
    Flowable<List<DayMeal>> getAllDayMeals();
    @Query("SELECT * FROM day_meal_table WHERE meal_date = :date")
    Flowable<List<DayMeal>> getDayMealListByDate(String date);
}
