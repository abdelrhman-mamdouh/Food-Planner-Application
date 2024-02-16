package com.example.foodzarella.country.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;
import com.example.foodzarella.country.model.MealCountry;

import java.util.List;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private List<MealCountry> countryItems;
    private final Context context;

    private Fragment fragment;

    public CountryAdapter(List<MealCountry> countryItems, Context context, Fragment fragment) {
        this.countryItems = countryItems;
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealCountry item = countryItems.get(position);
        holder.categoryNameTextView.setText(item.getStrCountry());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryName = countryItems.get(holder.getAdapterPosition()).getStrCountry();
                Bundle bundle = new Bundle();
                bundle.putString("countryName", countryName);
                NavController navController = NavHostFragment.findNavController(fragment);
                navController.popBackStack();
                navController.navigate(R.id.navigation_search, bundle);

            }
        });
    }

    @Override
    public int getItemCount() {
        return countryItems != null ? countryItems.size() : 0;
    }

    public void setList(List<MealCountry> categories) {
        this.countryItems = categories;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryNameTextView;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            layout = itemView.findViewById(R.id.cardViewCategory);
        }
    }
}