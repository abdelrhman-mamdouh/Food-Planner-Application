package com.example.foodzarella.dayMeal.view.mealPlan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.foodzarella.R;
import com.example.foodzarella.SnackbarUtils;
import com.example.foodzarella.dayMeal.presenter.DayMealsPresenter;
import com.example.foodzarella.dayMeal.presenter.DayPresenterImpl;
import com.example.foodzarella.dayMeal.view.DayMealAdapter;
import com.example.foodzarella.dayMeal.view.DayMealView;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.getMeals.MealsRemoteDataSource;
import com.example.foodzarella.network.getMeals.MealsRemoteSourceDataImpl;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealPlanFragment extends Fragment implements CalendarAdapter.OnItemListener, DayMealView {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button previousWeekButton;
    private Button nextWeekButton;
    private Button newEventAction;
    private Button addAnotherMeal;
    LottieAnimationView backgroundAnimationView;
    DayMealAdapter dayMealAdapter;

    private MealsLocalDataSource localDataSource;
    private MealsRemoteDataSource remoteDataSource;
    private RecyclerView eventRecyclerView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_meal_plan, container, false);
        initWidgets(view);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        boolean sharedPreferencesExists = !sharedPreferences.getAll().isEmpty();
        if (sharedPreferencesExists) {
            newEventAction = view.findViewById(R.id.newEventAction);
            newEventAction.setVisibility(View.VISIBLE);
        } else {
            newEventAction.setVisibility(View.GONE);
        }
        if (CalendarUtils.selectedDate == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CalendarUtils.selectedDate = LocalDate.now(); // or any other appropriate initialization
            }
        }
        setWeekView();
        return view;
    }

    private void initWidgets(View view) {
        backgroundAnimationView = view.findViewById(R.id.loaderss);
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        monthYearText = view.findViewById(R.id.monthYearTV);
        eventRecyclerView = view.findViewById(R.id.eventListView);
        previousWeekButton = view.findViewById(R.id.previousWeekAction);
        nextWeekButton = view.findViewById(R.id.nextWeekAction);
        newEventAction = view.findViewById(R.id.newEventAction);
        addAnotherMeal= view.findViewById(R.id.btn_selectMeal);
        addAnotherMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
                navController.navigate(R.id.navigation_home);
            }
        });
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
        monthYearText.setText(CalendarUtils.monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = CalendarUtils.daysInWeekArray(CalendarUtils.selectedDate);
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
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        eventRecyclerView.setLayoutManager(linearLayoutManager);

        localDataSource = MealsLocalDataSourceImpl.getInstance(requireContext());
        remoteDataSource = MealsRemoteSourceDataImpl.getInstance();
        CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        dayMealAdapter = new DayMealAdapter(null, requireContext(), localDataSource, remoteDataSource);
        eventRecyclerView.setAdapter(dayMealAdapter);

        if (isNetworkAvailable()) {
            fetchRemoteData();
        } else {
            fetchLocalData();
        }


    }

    private void fetchRemoteData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            getDayMealsFromFirestore(userId, new OnMealsLoadedListeners() {
                @Override
                public void onMealsLoaded(List<DayMeal> meals) {
                    dayMealAdapter.setList(meals);
                    dayMealAdapter.notifyDataSetChanged();
                    if (isDatasetEmpty()) {
                        showLoader();
                    } else {
                        hideLoader();
                        dayMealAdapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    e.printStackTrace();
                    showErrMsg("Failed to fetch data from Firestore");
                }
            });
        }
    }

    public interface OnMealsLoadedListeners {
        void onMealsLoaded(List<DayMeal> meals);

        void onFailure(Exception e);
    }
    private boolean isDatasetEmpty() {
        if (dayMealAdapter != null) {
            return dayMealAdapter.getItemCount() == 0;
        }
        return true;
    }

    public void getDayMealsFromFirestore(String userId, OnMealsLoadedListeners listener) {
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("dayMeals")
                .whereEqualTo("selectedDate", CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.isEmpty()) {
                    listener.onMealsLoaded(Collections.emptyList());
                } else {
                    List<DayMeal> mealList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        DayMeal meal = document.toObject(DayMeal.class);
                        mealList.add(meal);
                    }
                    listener.onMealsLoaded(mealList);
                }
            } else {
                listener.onFailure(task.getException());
            }
        });
    }

    private void fetchLocalData() {
        DayMealsPresenter dayMealsPresenter = new DayPresenterImpl(this, MealsRepositoryImol.getInstance(remoteDataSource, localDataSource));
        showData(dayMealsPresenter.getMealsByDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate)));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void newEventAction(View view) {
        LocalTime time;
        DayMeal meal;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        String mealJson = sharedPreferences.getString("meal", null);
        if (mealJson != null) {
            Gson gson = new Gson();
            meal = gson.fromJson(mealJson, DayMeal.class);
            meal.setSelectedDate(CalendarUtils.formattedDate(CalendarUtils.selectedDate));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Event newEvent = new Event("eventName", CalendarUtils.selectedDate, LocalTime.now());
            }
            addMealToDayMeals(userId, meal);

            MealsRepositoryImol.getInstance(
                    MealsRemoteSourceDataImpl.getInstance(),
                    MealsLocalDataSourceImpl.getInstance(getContext())
            ).insertDayMeal(meal);
        } else {

        }

    }

    @Override
    public void showData(Flowable<List<DayMeal>> listFlowable) {
        listFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        mealList -> {
                            dayMealAdapter.setList(mealList);
                            dayMealAdapter.notifyDataSetChanged();
                            hideLoader();
                        },
                        throwable -> {
                            showErrMsg(throwable.getMessage());
                        }
                );
    }
    private void showLoader() {
        backgroundAnimationView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        backgroundAnimationView.setVisibility(View.GONE);
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


    public void addMealToDayMeals(String userId, DayMeal meal) {
        CollectionReference dayMealsRef = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("dayMeals");
        dayMealsRef.add(meal)
                .addOnSuccessListener(documentReference -> {
                    if (isNetworkAvailable()) {
                        fetchRemoteData();
                        SnackbarUtils.showSnackbar(requireContext(),getView(),"Added");
                    }
                })
                .addOnFailureListener(e -> {

                });
    }
    public void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
