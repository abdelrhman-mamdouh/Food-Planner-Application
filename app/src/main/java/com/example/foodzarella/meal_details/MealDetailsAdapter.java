package com.example.foodzarella.meal_details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodzarella.R;

import java.util.List;

public class MealDetailsAdapter extends RecyclerView.Adapter<MealDetailsAdapter.ViewHolder> {

    private List<String> ingredients;
    private List<String> measures;
    private Context context;

    public MealDetailsAdapter(Context context, List<String> ingredients, List<String> measures) {
        this.context = context;
        this.ingredients = ingredients;
        this.measures = measures;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_details_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvIngredient.setText(ingredients.get(position));
        holder.tvMeasure.setText(measures.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void setList(List<String> ingredients, List<String> measures) {
        this.ingredients=ingredients;
        this.measures=measures;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIngredient;
        TextView tvMeasure;

        public ViewHolder(View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tv_Ingredient);
            tvMeasure = itemView.findViewById(R.id.tv_measure);
        }
    }
}
