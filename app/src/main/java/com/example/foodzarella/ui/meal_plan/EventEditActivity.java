package com.example.foodzarella.ui.meal_plan;

import static com.example.foodzarella.SnackbarUtils.showSnackbar;
import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.R;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;
import com.google.gson.Gson;

import java.time.LocalTime;

public class EventEditActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TextView eventDateTV, eventTimeTV;
    ImageView imageView;
    TextView mealName;
    private LocalTime time;
    DayMeal meal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String mealJson = sharedPreferences.getString("meal", null);
            Gson gson = new Gson();
             meal = gson.fromJson(mealJson, DayMeal.class);

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            time = LocalTime.now();
        }
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));

        String savedMealThumb = meal.getStrMealThumb();
        Glide.with(getApplicationContext())
                .load(savedMealThumb).apply(new RequestOptions().override(150, 150))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);
        mealName.setText(meal.getStrMeal());
    }

    private void initWidgets() {

        eventNameET = findViewById(R.id.eventNameET);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        imageView = findViewById(R.id.iv_meal);
        mealName = findViewById(R.id.tv_meal_name);
    }

    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        Event newEvent = new Event(eventName, CalendarUtils.selectedDate, time);
                MealsRepositoryImol.getInstance(
                        MealsRemoteSourceDataImpl.getInstance(),
                        MealsLocalDataSourceImpl.getInstance(getApplicationContext())
                ).insertDayMeal(meal);
        showSnackbar(getApplicationContext(), view, "Meal added to favorites");
        Event.eventsList.add(newEvent);
        finish();
    }
}