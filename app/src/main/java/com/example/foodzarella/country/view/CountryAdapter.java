package com.example.foodzarella.country.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealCountry item = countryItems.get(position);
        holder.categoryNameTextView.setText(item.getStrCountry());
        if(item.getStrCountry().equals("Unknown")){
            holder.categoryNameTextView.setText("Aruba");
        }
        String countryName =getCountryCode(item.getStrCountry());
        String flagUrl = "https://flagsapi.com/"+countryName+"/flat/64.png";

        Glide.with(context)
                .load(flagUrl)
                .into(holder.flagView);

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
        ImageView flagView;
        TextView categoryNameTextView;
        CardView layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            layout = itemView.findViewById(R.id.cardViewCategory);
            flagView= itemView.findViewById(R.id.imageViewGategory);
        }
    }


    private static String getCountryCode(String countryName) {
        switch (countryName) {
            case "American":
                return "US";
            case "British":
                return "GB";
            case "Canadian":
                return "CA";
            case "Chinese":
                return "CN";
            case "Croatian":
                return "HR";
            case "Dutch":
                return "NL";
            case "Egyptian":
                return "EG";
            case "Filipino":
                return "PH";
            case "French":
                return "FR";
            case "Greek":
                return "GR";
            case "Indian":
                return "IN";
            case "Irish":
                return "IE";
            case "Italian":
                return "IT";
            case "Jamaican":
                return "JM";
            case "Japanese":
                return "JP";
            case "Kenyan":
                return "KE";
            case "Malaysian":
                return "MY";
            case "Mexican":
                return "MX";
            case "Moroccan":
                return "MA";
            case "Polish":
                return "PL";
            case "Portuguese":
                return "PT";
            case "Russian":
                return "RU";
            case "Spanish":
                return "ES";
            case "Thai":
                return "TH";
            case "Tunisian":
                return "TN";
            case "Turkish":
                return "TR";
            case "Unknown":
                return "AW"; // Replace "XX" with the appropriate code for "Unknown"
            case "Vietnamese":
                return "VN";
            default:
                return null; // Unknown country code
        }
    }
}