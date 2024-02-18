package com.example.foodzarella.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity(tableName = "day_meal_table")
public class DayMeal {
    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public void setIdMeal(@NonNull String idMeal) {
        this.idMeal = idMeal;
    }

    @ColumnInfo(name = "meal_date")
    private String selectedDate;
    @ColumnInfo(name = "meal_name")
    private String strMeal;
    @ColumnInfo(name = "meal_image_url")
    private String strMealThumb;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "meal_id")
    private String idMeal;
    public DayMeal(){

    }
    public DayMeal(String strMeal, String strMealThumb, String idMeal, String selectedDate) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
        this.selectedDate = selectedDate;
    }

    public String getStrMeal() {
        return strMeal;
    }


    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getIdMeal() {
        return idMeal;
    }

}
