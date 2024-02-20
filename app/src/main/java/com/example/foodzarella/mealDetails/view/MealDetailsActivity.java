package com.example.foodzarella.mealDetails.view;

import static android.app.PendingIntent.getActivity;
import static com.example.foodzarella.SnackbarUtils.showSnackbar;
import static com.example.foodzarella.db.MealsLocalDataSourceImpl.mealsLocalDataSource;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodzarella.HomeActivity;
import com.example.foodzarella.MainActivity;
import com.example.foodzarella.R;
import com.example.foodzarella.SnackbarUtils;
import com.example.foodzarella.allMeals.view.MealAdapter;
import com.example.foodzarella.allMeals.view.ViewHolderMeals;
import com.example.foodzarella.dayMeal.view.mealPlan.MealPlanFragment;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.db.MealsLocalDataSourceImpl;
import com.example.foodzarella.mealDetails.model.MealDetails;
import com.example.foodzarella.mealDetails.presenter.MealDetailsPresenter;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.getMeals.MealQuery;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsView {
    private MealDetailsPresenter presenter;
    private ImageView imageViewHero;
    private TextView tvArea;
    private TextView tvMealName;
    private TextView tvCategory;
    private AppCompatImageButton favoriteButton;
    private TextView tvInstructions;
    private TextView textViewIngredients;
    private RecyclerView recyclerView;
    YouTubePlayerView youTubePlayerView;
    String mealId;
    Meal mealFav;
    AppCompatImageButton mealPlan;
    MealsLocalDataSource mealsLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;

    AppCompatImageButton add;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        add = findViewById(R.id.favoriteButton);
        mealPlan = findViewById(R.id.btn_meal_plan);
        isFavorite = false;
        mealsLocalDataSource =  MealsLocalDataSourceImpl.getInstance(getApplicationContext());
        mealsRemoteDataSource= MealsRemoteSourceDataImpl.getInstance();
        SharedPreferences sharedPreferences = this.getSharedPreferences("meal", Context.MODE_PRIVATE);
        String mealJson = sharedPreferences.getString("mealFav", null);
        if (mealJson != null) {
            Gson gson = new Gson();
            mealFav = gson.fromJson(mealJson, Meal.class);

        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    removeMealFromFavorites(userId, mealFav);
                    showSnackbar(getApplicationContext(), v, "Meal removed from favorites");
                } else {
                    addMealToFavorites(userId, mealFav);
                    showSnackbar(getApplicationContext(), v, "Meal added to favorites");
                }
                isFavorite = !isFavorite;
            }
        });
        if (isNetworkAvailable()) {
            checkFavoriteMealExistenceForUser(userId, mealFav, new MealAdapter.OnFavoriteMealExistenceCheckedListener() {
                        @Override
                        public void onFavoriteMealExistenceChecked(boolean mealExists) {
                            if (mealExists) {
                                add.setImageResource(R.drawable.ic_favorite_on);
                                isFavorite = true;
                            } else {
                                add.setImageResource(R.drawable.ic_favorite_off);
                                isFavorite = false;
                            }
                        }
                    }
            );
        } else {
            fetchLocalData(mealFav);
        }

        mealPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DayMeal dayMeal = new DayMeal(mealFav.getStrMeal(), mealFav.getStrMealThumb(), mealFav.getIdMeal(), "");
                Gson gson = new Gson();
                String mealJson = gson.toJson(dayMeal);

                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("meal", mealJson);
                editor.apply();
                
            }
        });



    mealId = getIntent().getStringExtra("ID_KEY");
        imageViewHero = findViewById(R.id.imageview_hero);
        tvArea = findViewById(R.id.tv_area);
        tvMealName = findViewById(R.id.tv_meal_name);
        tvCategory = findViewById(R.id.tv_gategory);
        favoriteButton = findViewById(R.id.favoriteButton);
        tvInstructions = findViewById(R.id.tv_Instructions);
        textViewIngredients = findViewById(R.id.textView4);
        youTubePlayerView = findViewById(R.id.videoView);

        recyclerView = findViewById(R.id.recyclerViewDetails);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        presenter = new MealDetailsPresenter(ApiClient.getClient().create(MealQuery.class), this);
        presenter.getMealDetails(mealId);

    }

    @Override
    public void displayMealDetails(MealDetails mealDetails) {
        tvMealName.setText(mealDetails.getStrMeal());

        tvArea.setText(mealDetails.getStrArea());

        tvCategory.setText(mealDetails.getStrCategory());

        tvInstructions.setText(mealDetails.getStrInstructions());


        Glide.with(this).load(mealDetails.getStrMealThumb()).into(imageViewHero);

        String videoUrl = mealDetails.getStrYoutube();
        String videoId = getVideoIdFromUrl(videoUrl);

        initializeYouTubePlayer(videoId);

       MealDetailsAdapter adapter = new MealDetailsAdapter(getApplicationContext(), addIngredientsList(mealDetails), addMeasuresList(mealDetails));
         adapter = new MealDetailsAdapter(getApplicationContext(), addIngredientsList(mealDetails), addMeasuresList(mealDetails));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (adapter != null) {
            adapter.setList(addIngredientsList(mealDetails), addMeasuresList(mealDetails));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showErrorMessage() {
    }

    public List<String> addIngredientsList(MealDetails mealDetails) {
        List<String> ingredientsList = new ArrayList<>();
        addIngredient(ingredientsList, mealDetails.getStrIngredient1());
        addIngredient(ingredientsList, mealDetails.getStrIngredient2());
        addIngredient(ingredientsList, mealDetails.getStrIngredient3());
        addIngredient(ingredientsList, mealDetails.getStrIngredient4());
        addIngredient(ingredientsList, mealDetails.getStrIngredient5());
        addIngredient(ingredientsList, mealDetails.getStrIngredient6());
        addIngredient(ingredientsList, mealDetails.getStrIngredient7());
        addIngredient(ingredientsList, mealDetails.getStrIngredient8());
        addIngredient(ingredientsList, mealDetails.getStrIngredient9());
        addIngredient(ingredientsList, mealDetails.getStrIngredient10());
        addIngredient(ingredientsList, mealDetails.getStrIngredient11());
        addIngredient(ingredientsList, mealDetails.getStrIngredient12());
        addIngredient(ingredientsList, mealDetails.getStrIngredient13());
        addIngredient(ingredientsList, mealDetails.getStrIngredient14());
        addIngredient(ingredientsList, mealDetails.getStrIngredient15());
        addIngredient(ingredientsList, mealDetails.getStrIngredient16());
        addIngredient(ingredientsList, mealDetails.getStrIngredient17());
        addIngredient(ingredientsList, mealDetails.getStrIngredient18());
        addIngredient(ingredientsList, mealDetails.getStrIngredient19());
        addIngredient(ingredientsList, mealDetails.getStrIngredient20());
        return ingredientsList;
    }

    public List<String> addMeasuresList(MealDetails mealDetails) {
        List<String> measuresList = new ArrayList<>();
        addMeasure(measuresList, mealDetails.getStrMeasure1());
        addMeasure(measuresList, mealDetails.getStrMeasure2());
        addMeasure(measuresList, mealDetails.getStrMeasure3());
        addMeasure(measuresList, mealDetails.getStrMeasure4());
        addMeasure(measuresList, mealDetails.getStrMeasure5());
        addMeasure(measuresList, mealDetails.getStrMeasure6());
        addMeasure(measuresList, mealDetails.getStrMeasure7());
        addMeasure(measuresList, mealDetails.getStrMeasure8());
        addMeasure(measuresList, mealDetails.getStrMeasure9());
        addMeasure(measuresList, mealDetails.getStrMeasure10());
        addMeasure(measuresList, mealDetails.getStrMeasure11());
        addMeasure(measuresList, mealDetails.getStrMeasure12());
        addMeasure(measuresList, mealDetails.getStrMeasure13());
        addMeasure(measuresList, mealDetails.getStrMeasure14());
        addMeasure(measuresList, mealDetails.getStrMeasure15());
        addMeasure(measuresList, mealDetails.getStrMeasure16());
        addMeasure(measuresList, mealDetails.getStrMeasure17());
        addMeasure(measuresList, mealDetails.getStrMeasure18());
        addMeasure(measuresList, mealDetails.getStrMeasure19());
        addMeasure(measuresList, mealDetails.getStrMeasure20());
        return measuresList;
    }

    private void addIngredient(List<String> list, String ingredient) {
        if (ingredient != null && !ingredient.isEmpty()) {
            list.add(ingredient);
        }
    }

    private void addMeasure(List<String> list, String measure) {
        if (measure != null && !measure.isEmpty()) {
            list.add(measure);
        }
    }
    private void initializeYouTubePlayer(String videoId) {
        youTubePlayerView.setEnableAutomaticInitialization(false);
        youTubePlayerView.initialize(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                youTubePlayer.cueVideo(videoId, 0);
            }
        });
    }
    private String getVideoIdFromUrl(String videoUrl) {
        return videoUrl.substring(videoUrl.lastIndexOf("=") + 1);
    }
    public void addMealToFavorites(String userId, Meal meal) {
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("favoriteMeals")
                .whereEqualTo("idMeal", meal.getIdMeal())
                .limit(1);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.isEmpty()) {
                    CollectionReference favoriteMealsRef = FirebaseFirestore.getInstance().collection("users").document(userId).collection("favoriteMeals");
                    favoriteMealsRef.add(meal).addOnSuccessListener(documentReference -> {
                        add.setImageResource(R.drawable.ic_favorite_on);
                        MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);
                    }).addOnFailureListener(e -> {
                        add.setImageResource(R.drawable.ic_favorite_on);
                        MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);
                    });
                } else {
                    add.setImageResource(R.drawable.ic_favorite_on);
                    MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);

                }
            } else {
                add.setImageResource(R.drawable.ic_favorite_on);
                MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);
            }
        });
    }

    public void removeMealFromFavorites(String userId, Meal meal) {
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("favoriteMeals")
                .whereEqualTo("idMeal", meal.getIdMeal())
                .limit(1); // Limit to 1 document since we only need to find one
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    documentSnapshot.getReference().delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            add.setImageResource(R.drawable.ic_favorite_off);
                            MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
                        } else {
                            add.setImageResource(R.drawable.ic_favorite_off);
                            MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
                        }
                    });
                } else {
                    add.setImageResource(R.drawable.ic_favorite_off);
                    MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
                }
            } else {
                add.setImageResource(R.drawable.ic_favorite_off);
                MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkFavoriteMealExistenceForUser(String userId, Meal meal, MealAdapter.OnFavoriteMealExistenceCheckedListener listener) {
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("favoriteMeals")
                .whereEqualTo("idMeal", meal.getIdMeal())
                .limit(1); // Limit to 1 document since we only need to check existence

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean mealExists = !task.getResult().isEmpty();
                listener.onFavoriteMealExistenceChecked(mealExists);
            } else {
                listener.onFavoriteMealExistenceChecked(false);
            }
        });
    }

    private void fetchLocalData(Meal meal) {
        mealsLocalDataSource.isMealExists(meal.getIdMeal())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean exists) {
                        if (exists) {
                            add.setImageResource(R.drawable.ic_favorite_on);
                            isFavorite = true;
                        } else {
                            add.setImageResource(R.drawable.ic_favorite_off);
                            isFavorite = false;
                        }
                    }
                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {
                        // Not needed for Flowable, can be left empty
                    }
                });
    }

    public interface OnFavoriteMealExistenceCheckedListener {
        void onFavoriteMealExistenceChecked(boolean mealExists);
    }
}