package com.example.foodzarella.favMeals.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.MainActivity;
import com.example.foodzarella.R;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.mealDetails.view.MealDetailsActivity;
import com.example.foodzarella.model.Meal;
import com.example.foodzarella.model.MealsRepositoryImol;
import com.example.foodzarella.network.getMeals.MealsRemoteDataSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private List<Meal> mealList;
    private final Context context;
    MealsLocalDataSource mealsLocalDataSource;
    MealsRemoteDataSource mealsRemoteDataSource;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userId;
    private FirebaseFirestore db;

    public FavAdapter(List<Meal> productList, Context context, MealsLocalDataSource localDataSource, MealsRemoteDataSource remoteDataSource) {
        this.mealList = productList;
        this.context = context;
        this.mealsLocalDataSource = localDataSource;
        this.mealsRemoteDataSource = remoteDataSource;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_meal, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Meal currentMeal = mealList.get(position);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userId = currentUser.getUid();

        holder.titleTextView.setText(currentMeal.getStrMeal());
        holder.remove.setOnClickListener(v -> {
            if(!currentUser.isAnonymous()){
            if (isNetworkAvailable()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to remove this meal from favorites?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeMealFromFavorites(userId, currentMeal);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(currentMeal);
            }
            }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("You need to sign in to remove meal from favorites. Do you want to sign in?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
            
        });
        String url = currentMeal.getStrMealThumb();
        Glide.with(context)
                .load(url).apply(new RequestOptions().override(200, 200))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MealDetailsActivity.class);
                intent.putExtra("ID_KEY", currentMeal.getIdMeal());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mealList != null ? mealList.size() : 0;
    }

    public void setList(List<Meal> updatedMeals) {
        this.mealList = updatedMeals;
    }

    public void removeMealFromFavorites(String userId, Meal meal) {
        Query query = FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("favoriteMeals")
                .whereEqualTo("idMeal", meal.getIdMeal())
                .limit(1);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                    documentSnapshot.getReference().delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            int position = mealList.indexOf(meal);
                            mealList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mealList.size());
                            MealsRepositoryImol.getInstance(mealsRemoteDataSource, mealsLocalDataSource).delete(mealList.get(position));
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

}