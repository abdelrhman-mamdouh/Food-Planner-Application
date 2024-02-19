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
        String countryName = item.getStrCountry();
        String flagEmoji = getFlagEmoji(countryName); // Convert the flag emoji to drawable
        Drawable flagDrawable = getDrawableFromEmoji(flagEmoji, holder.itemView);
        holder.flagView.setImageDrawable(flagDrawable);
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
    private Drawable getDrawableFromEmoji(String emoji, View itemView) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(48);
        paint.setTextAlign(Paint.Align.LEFT);
        int width = (int) paint.measureText(emoji);
        int height = (int) (-paint.ascent() + paint.descent());
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(emoji, 0, -paint.ascent(), paint);
        return new BitmapDrawable(itemView.getResources(), bitmap);
    }


    private String getFlagEmoji(String countryName) {
        switch (countryName.toLowerCase()) {
            case "american":
                return "\uD83C\uDDFA\uD83C\uDDF8";
            case "british":
                return "\uD83C\uDDEC\uD83C\uDDE7";
            case "canadian":
                return "\uD83C\uDDE8\uD83C\uDDE6";
            case "chinese":
                return "\uD83C\uDDE8\uD83C\uDDF3";
            case "croatian":
                return "\uD83C\uDDED\uD83C\uDDF7";
            case "dutch":
                return "\uD83C\uDDF3\uD83C\uDDF1";
            case "egyptian":
                return "\uD83C\uDDEA\uD83C\uDDEC";
            case "filipino":
                return "\uD83C\uDDEB\uD83C\uDDEE";
            case "french":
                return "\uD83C\uDDEB\uD83C\uDDF7";
            case "greek":
                return "\uD83C\uDDEC\uD83C\uDDF7";
            case "indian":
                return "\uD83C\uDDEE\uD83C\uDDF3";
            case "irish":
                return "\uD83C\uDDEE\uD83C\uDDEA";
            case "italian":
                return "\uD83C\uDDEE\uD83C\uDDF9";
            case "jamaican":
                return "\uD83C\uDDEF\uD83C\uDDF2";
            case "japanese":
                return "\uD83C\uDDEF\uD83C\uDDF5";
            case "kenyan":
                return "\uD83C\uDDF0\uD83C\uDDF2";
            case "malaysian":
                return "\uD83C\uDDF2\uD83C\uDDFE";
            case "mexican":
                return "\uD83C\uDDF2\uD83C\uDDFD";
            case "moroccan":
                return "\uD83C\uDDF2\uD83C\uDDE6";
            case "polish":
                return "\uD83C\uDDF5\uD83C\uDDF1";
            case "portuguese":
                return "\uD83C\uDDF5\uD83C\uDDF9";
            case "russian":
                return "\uD83C\uDDF7\uD83C\uDDFA";
            case "spanish":
                return "\uD83C\uDDEA\uD83C\uDDF8";
            case "thai":
                return "\uD83C\uDDF9\uD83C\uDDED";
            case "tunisian":
                return "\uD83C\uDDF9\uD83C\uDDF3";
            case "turkish":
                return "\uD83C\uDDF9\uD83C\uDDF7";
            case "palastine":
                return "\uD83C\uDDF5\uD83C\uDDF8";
            case "vietnamese":
                return "\uD83C\uDDFB\uD83C\uDDF3";
            default:
                return "\uD83C\uDDF5\uD83C\uDDF8";
        }
    }

}