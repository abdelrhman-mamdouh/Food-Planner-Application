package com.example.foodzarella.ui.meal_plan;
import static com.example.foodzarella.ui.meal_plan.CalendarUtils.daysInWeekArray;
import static com.example.foodzarella.ui.meal_plan.CalendarUtils.monthYearFromDate;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;
import com.example.foodzarella.day_meal.presenter.DayMealsPresenter;
import com.example.foodzarella.day_meal.presenter.DayPresenterImpl;
import com.example.foodzarella.day_meal.view.DayMealAdapter;
import com.example.foodzarella.day_meal.view.DayMealView;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;
import com.example.foodzarella.network.get_meals.MealsRemoteSourceDataImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MealPlanFragment extends Fragment implements CalendarAdapter.OnItemListener, DayMealView {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button previousWeekButton;
    private Button nextWeekButton;
    private Button newEventAction;

    DayMealAdapter dayMealAdapter;

    private MealsLocalDataSource localDataSource;
    private MealsRemoteDataSource remoteDataSource;
    private RecyclerView eventRecyclerView; // Change from ListView to RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_meal_plan, container, false);
        initWidgets(view);

        if (CalendarUtils.selectedDate == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CalendarUtils.selectedDate = LocalDate.now(); // or any other appropriate initialization
            }
        }
        setWeekView();
        return view;
    }

    private void initWidgets(View view) {
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventRecyclerView = view.findViewById(R.id.eventListView);
        previousWeekButton = view.findViewById(R.id.previousWeekAction);
        nextWeekButton = view.findViewById(R.id.nextWeekAction);
        newEventAction = view.findViewById(R.id.newEventAction);
        newEventAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEventAction(v);
            }
        });
        previousWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeekAction();
            }
        });
        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeekAction();
            }
        });
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();
    }

    public void previousWeekAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        }
        setWeekView();
    }

    public void nextWeekAction() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        }
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    public void onResume() {
        super.onResume();
       setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getContext(), dailyEvents);
        eventRecyclerView.setAdapter(eventAdapter);
        eventRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        eventRecyclerView.setLayoutManager(linearLayoutManager);

        localDataSource = MealsLocalDataSourceImpl.getInstance(requireContext());
        remoteDataSource = MealsRemoteSourceDataImpl.getInstance();
        CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        dayMealAdapter = new DayMealAdapter(null, requireContext(), localDataSource, remoteDataSource);
        eventRecyclerView.setAdapter(dayMealAdapter);

        DayMealsPresenter dayMealsPresenter = new DayPresenterImpl(this, MealsRepositoryImol.getInstance(remoteDataSource, localDataSource));
        showData(dayMealsPresenter.getMealsByDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate)));

    }

    public void newEventAction(View view) {
        startActivity(new Intent(getActivity(), EventEditActivity.class));
    }

    @Override
    public void showData(LiveData<List<DayMeal>> listLiveData) {
        listLiveData.observe(getViewLifecycleOwner(), new Observer<List<DayMeal>>() {
            @Override
            public void onChanged(List<DayMeal> mealList) {
                dayMealAdapter.setList(mealList);
                dayMealAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showErrMsg(String error) {
        new AlertDialog.Builder(requireContext())
                .setMessage(error)
                .setTitle("An Error Occurred")
                .show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}
