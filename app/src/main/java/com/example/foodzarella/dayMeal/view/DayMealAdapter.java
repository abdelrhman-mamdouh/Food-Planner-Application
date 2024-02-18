package com.example.foodzarella.dayMeal.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.R;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.model.DayMeal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class DayMealAdapter extends RecyclerView.Adapter<DayMealViewHolder> {
    private List<DayMeal> mealList;
    private final Context context;
    MealsLocalDataSource mealsLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;
    public DayMealAdapter(List<DayMeal> dayMeals, Context context, MealsLocalDataSource localDataSource, MealsRemoteDataSource remoteDataSource) {
        this.mealList = dayMeals;
        this.context = context;
        this.mealsLocalDataSource = localDataSource;
        this.mealsRemoteDataSource = remoteDataSource;
    }

    @NonNull
    @Override
    public DayMealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.favorite_meal, parent, false);
        return new DayMealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayMealViewHolder holder, int position) {
        DayMeal currentMeal = mealList.get(position);
        holder.titleTextView.setText(currentMeal.getStrMeal());
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        holder.remove.setOnClickListener(v -> {
            if (isNetworkAvailable()) {
                removeMealFromDayMeals(userId, currentMeal);
            } else
                MealsRepositoryImol.getInstance(mealsRemoteDataSource,mealsLocalDataSource).deleteDayMeal(currentMeal);

        });

        String url = currentMeal.getStrMealThumb();
        Glide.with(context)
                .load(url).apply(new RequestOptions().override(200,200))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.layout.setOnClickListener(view -> Toast.makeText(context, mealList.get(position).getStrMeal(), Toast.LENGTH_SHORT).show());
    }

    private void removeMealFromDayMeals(String userId, DayMeal currentMeal) {
        Query query = FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .collection("dayMeals")
            .whereEqualTo("idMeal", currentMeal.getIdMeal())
            .limit(1);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    documentSnapshot.getReference().delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            int position = mealList.indexOf(currentMeal);
                            mealList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mealList.size());

                        } else {
                        }
                    });
                } else {
                }
            } else {
            }
        });

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    } public void setList(List<DayMeal> updatedMeals){
        this.mealList =updatedMeals;
    }
}