package com.example.foodzarella.model;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "meal_table")
public class Meal {
    @ColumnInfo(name = "meal_name")
    private String strMeal;

    @ColumnInfo(name = "meal_image_url")
    private String strMealThumb;

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "meal_id")
    private String idMeal;

    // No-argument constructor
    public Meal() {
        // Required for Firestore to be able to deserialize the object
    }

    public Meal(String strMeal, String strMealThumb, String idMeal) {
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
        this.idMeal = idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }
}
