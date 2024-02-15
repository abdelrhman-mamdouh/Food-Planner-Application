package com.example.foodzarella.categorys.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.foodzarella.R;
import com.example.foodzarella.categorys.model.Category;
import com.example.foodzarella.db.MealsLocalDataSource;
import com.example.foodzarella.meal_details.MealDetailsActivity;
import com.example.foodzarella.network.get_meals.MealsRemoteDataSource;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GategoryAdapter extends RecyclerView.Adapter<GategoryAdapter.ViewHolder> {
    private List<Category> categoryItems;
    private final Context context;

    private Fragment fragment;

    public GategoryAdapter(List<Category> categoryItems, Context context, Fragment fragment) {
        this.categoryItems = categoryItems;
        this.context = context;
        this.fragment=fragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category item = categoryItems.get(position);
        holder.categoryNameTextView.setText(item.getStrCategory());
        String url = item.getStrCategoryThumb();
        Glide.with(context)
                .load(url).apply(new RequestOptions().override(200,200))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = categoryItems.get(holder.getAdapterPosition()).getStrCategory();
                Bundle bundle = new Bundle();
                bundle.putString("categoryName", categoryName);
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.popBackStack(); // Close the current fragment
                navController.navigate(R.id.navigation_search, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryItems != null ? categoryItems.size() : 0;
    }

    public void setList(List<Category> categories) {
        this.categoryItems = categories;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView categoryNameTextView;
        TextView descriptionTextView;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewGategory);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);

            layout = itemView.findViewById(R.id.cardViewCategory);
        }
    }
}