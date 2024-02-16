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
@Dao
public interface DayMealDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DayMeal meal);

    @Update
    void update(DayMeal meal);

    @Delete
    void delete(DayMeal meal);
    @Query("SELECT * FROM day_meal_table")
    LiveData<List<DayMeal>> getAllDayMeals();
    @Query("SELECT EXISTS (SELECT 1 FROM day_meal_table WHERE meal_id = :mealId)")
    LiveData<Boolean> isMealExists(String mealId);
}
