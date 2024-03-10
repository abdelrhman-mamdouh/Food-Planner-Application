package com.example.foodzarella.allMeals.view;

import static android.app.PendingIntent.getActivity;
import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.core.content.ContextCompat.startActivity;
import static com.example.foodzarella.SnackbarUtils.showSnackbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.HomeActivity;
import com.example.foodzarella.MainActivity;
import com.example.foodzarella.R;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.mealDetails.view.MealDetailsActivity;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.getMeals.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;


import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.DisposableSubscriber;

public class MealAdapter extends RecyclerView.Adapter<ViewHolderMeals> {
    private List<Meal> mealList;
    private final Context context;
    boolean isFavorite;
    private FirebaseFirestore db;
    MealsLocalDataSource mealsLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;

    public MealAdapter(List<Meal> mealList, Context context, MealsLocalDataSource localDataSource, MealsRemoteDataSource remoteDataSource) {
        this.mealList = mealList;
        this.context = context;
        this.mealsLocalDataSource = localDataSource;
        this.mealsRemoteDataSource = remoteDataSource;
        db = FirebaseFirestore.getInstance();
        isFavorite = false;
    }

    @NonNull
    @Override
    public ViewHolderMeals onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.meal_item3, parent, false);
        return new ViewHolderMeals(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMeals holder, int position) {
        Meal meal = mealList.get(position);
        holder.titleTextView.setText(meal.getStrMeal());

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentUser.isAnonymous()) {
                    if (isFavorite) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you sure you want to remove this meal from favorites?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeMealFromFavorites(userId, meal, holder);
                                showSnackbar(context, v, "Meal removed from favorites");
                                isFavorite = false;
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    } else {
                        addMealToFavorites(userId, meal, holder);
                        showSnackbar(context, v, "Meal added to favorites");
                        isFavorite = true;
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You need to sign in to add meals to favorites. Do you want to sign in?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            mAuth.signOut();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });

        if (isNetworkAvailable()) {
            checkFavoriteMealExistenceForUser(userId, meal, new OnFavoriteMealExistenceCheckedListener() {
                @Override
                public void onFavoriteMealExistenceChecked(boolean mealExists) {
                    if (mealExists) {
                        holder.add.setImageResource(R.drawable.ic_favorite_on);
                        isFavorite = true;
                    } else {
                        holder.add.setImageResource(R.drawable.ic_favorite_off);
                        isFavorite = false;
                    }
                }
            });
        } else {
            fetchLocalData(meal, holder);
        }

        holder.MealPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentUser.isAnonymous()) {
                    DayMeal dayMeal = new DayMeal(meal.getStrMeal(), meal.getStrMealThumb(), meal.getIdMeal(), "");
                    Gson gson = new Gson();
                    String mealJson = gson.toJson(dayMeal);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("meal", mealJson);
                    editor.apply();
                    NavController navController = Navigation.findNavController(v);
                    navController.popBackStack();
                    navController.navigate(R.id.navigation_meal_plan);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You need to sign in to add meals to calender. Do you want to sign in?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            mAuth.signOut();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        });
        String url = meal.getStrMealThumb();
        Glide.with(context).load(url).apply(new RequestOptions().override(150, 150)).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Meal mealFav = new Meal(meal.getStrMeal(), meal.getStrMealThumb(), meal.getIdMeal());
                Gson gson = new Gson();
                String mealJson = gson.toJson(mealFav);
                SharedPreferences sharedPreferences = context.getSharedPreferences("meal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mealFav", mealJson);
                editor.apply();

                Intent intent = new Intent(context, MealDetailsActivity.class);
                intent.putExtra("ID_KEY", meal.getIdMeal());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    public void setList(List<Meal> updatedProducts) {
        this.mealList = updatedProducts;
    }


    public void addMealToFavorites(String userId, Meal meal, ViewHolderMeals ViewHolderMeals) {
        Query query = FirebaseFirestore.getInstance().collection("users").document(userId).collection("favoriteMeals").whereEqualTo("idMeal", meal.getIdMeal()).limit(1);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.isEmpty()) {
                    CollectionReference favoriteMealsRef = FirebaseFirestore.getInstance().collection("users").document(userId).collection("favoriteMeals");
                    favoriteMealsRef.add(meal).addOnSuccessListener(documentReference -> {
                        ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_on);
                        MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);
                    }).addOnFailureListener(e -> {
                        ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_on);
                        MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);
                    });
                } else {
                    ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_on);
                    MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);

                }
            } else {
                ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_on);
                MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).insert(meal);
            }
        });
    }

    public void removeMealFromFavorites(String userId, Meal meal, ViewHolderMeals ViewHolderMeals) {
        Query query = FirebaseFirestore.getInstance().collection("users").document(userId).collection("favoriteMeals").whereEqualTo("idMeal", meal.getIdMeal()).limit(1); // Limit to 1 document since we only need to find one
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    documentSnapshot.getReference().delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_off);
                            MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
                        } else {
                            ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_off);
                            MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
                        }
                    });
                } else {
                    ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_off);
                    MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
                }
            } else {
                ViewHolderMeals.add.setImageResource(R.drawable.ic_favorite_off);
                MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(meal);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void checkFavoriteMealExistenceForUser(String userId, Meal meal, OnFavoriteMealExistenceCheckedListener listener) {
        Query query = FirebaseFirestore.getInstance().collection("users").document(userId).collection("favoriteMeals").whereEqualTo("idMeal", meal.getIdMeal()).limit(1); // Limit to 1 document since we only need to check existence

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean mealExists = !task.getResult().isEmpty();
                listener.onFavoriteMealExistenceChecked(mealExists);
            } else {
                listener.onFavoriteMealExistenceChecked(false);
            }
        });
    }

    private void fetchLocalData(Meal meal, ViewHolderMeals viewHolderMeals) {
        mealsLocalDataSource.isMealExists(meal.getIdMeal()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DisposableSubscriber<Boolean>() {
            @Override
            public void onNext(Boolean exists) {
                if (exists) {
                    viewHolderMeals.add.setImageResource(R.drawable.ic_favorite_on);
                    isFavorite = true;
                } else {
                    viewHolderMeals.add.setImageResource(R.drawable.ic_favorite_off);
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
