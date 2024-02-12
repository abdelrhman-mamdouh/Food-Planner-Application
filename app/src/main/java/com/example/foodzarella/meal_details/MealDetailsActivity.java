package com.example.foodzarella.meal_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.foodzarella.R;
import com.example.foodzarella.network.ApiClient;
import com.example.foodzarella.network.MealApiService;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MealDetailsActivity extends AppCompatActivity implements MealDetailsView{
    private MealDetailsPresenter presenter;
    private ImageView imageViewHero;
    private TextView tvArea;
    private TextView tvMealName;
    private TextView tvCategory;
    private AppCompatImageButton favoriteButton;
    private TextView tvInstructions;
    private TextView textViewIngredients;
    private RecyclerView recyclerView;
    YouTubePlayerView youTubePlayerView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);

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

        presenter = new MealDetailsPresenter(ApiClient.getClient().create(MealApiService.class), this);

        String mealId = getIntent().getStringExtra("ID_KEY");

        presenter.getMealDetails(mealId);
    }

    @Override
    public void displayMealDetails(MealDetails mealDetails) {
        tvMealName.setText(mealDetails.getStrMeal());

        tvArea.setText(mealDetails.getStrArea());

        tvCategory.setText(mealDetails.getStrCategory());

        tvInstructions.setText(mealDetails.getStrInstructions());


        Glide.with(this).load(mealDetails.getStrMealThumb()).into(imageViewHero);

        VideoAdapter videoAdapter=new VideoAdapter(youTubePlayerView);
        String videoUrl = mealDetails.getStrYoutube();
        if (videoUrl != null && !videoUrl.isEmpty()) {
             videoAdapter = new VideoAdapter(youTubePlayerView);
            videoAdapter.initializeWithUrl(getVideoIdFromUrl(videoUrl));
        } else {
            Toast.makeText(getApplicationContext(), "No video available", Toast.LENGTH_SHORT).show();
        }


        Toast.makeText(getApplicationContext(),getVideoIdFromUrl(videoUrl)+"",Toast.LENGTH_SHORT).show();


        Toast.makeText(getApplicationContext(), addIngredientsList(mealDetails)+"",Toast.LENGTH_SHORT).show();

        MealDetailsAdapter adapter = new MealDetailsAdapter(getApplicationContext(), addIngredientsList(mealDetails), addMeasuresList(mealDetails));
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

    List<String> addIngredientsList(MealDetails mealDetails){
        List<String> ingredientsList = new ArrayList<>();
        ingredientsList.add(mealDetails.getStrIngredient1());
        ingredientsList.add(mealDetails.getStrIngredient2());
        ingredientsList.add(mealDetails.getStrIngredient3());
        ingredientsList.add(mealDetails.getStrIngredient4());
        ingredientsList.add(mealDetails.getStrIngredient5());
        ingredientsList.add(mealDetails.getStrIngredient6());
        ingredientsList.add(mealDetails.getStrIngredient7());
        ingredientsList.add(mealDetails.getStrIngredient8());
        ingredientsList.add(mealDetails.getStrIngredient9());
        ingredientsList.add(mealDetails.getStrIngredient10());
        ingredientsList.add(mealDetails.getStrIngredient11());
        ingredientsList.add(mealDetails.getStrIngredient12());
        ingredientsList.add(mealDetails.getStrIngredient13());
        ingredientsList.add(mealDetails.getStrIngredient14());
        ingredientsList.add(mealDetails.getStrIngredient15());
        ingredientsList.add(mealDetails.getStrIngredient16());
        ingredientsList.add(mealDetails.getStrIngredient17());
        ingredientsList.add(mealDetails.getStrIngredient18());
        ingredientsList.add(mealDetails.getStrIngredient19());
        ingredientsList.add(mealDetails.getStrIngredient20());
        return ingredientsList;
    }
    List<String> addMeasuresList(MealDetails mealDetails){
        List<String> measuresList = new ArrayList<>();
        measuresList.add(mealDetails.getStrMeasure1());
        measuresList.add(mealDetails.getStrMeasure2());
        measuresList.add(mealDetails.getStrMeasure3());
        measuresList.add(mealDetails.getStrMeasure4());
        measuresList.add(mealDetails.getStrMeasure5());
        measuresList.add(mealDetails.getStrMeasure6());
        measuresList.add(mealDetails.getStrMeasure7());
        measuresList.add(mealDetails.getStrMeasure8());
        measuresList.add(mealDetails.getStrMeasure9());
        measuresList.add(mealDetails.getStrMeasure10());
        measuresList.add(mealDetails.getStrMeasure11());
        measuresList.add(mealDetails.getStrMeasure12());
        measuresList.add(mealDetails.getStrMeasure13());
        measuresList.add(mealDetails.getStrMeasure14());
        measuresList.add(mealDetails.getStrMeasure15());
        measuresList.add(mealDetails.getStrMeasure16());
        measuresList.add(mealDetails.getStrMeasure17());
        measuresList.add(mealDetails.getStrMeasure18());
        measuresList.add(mealDetails.getStrMeasure19());
        measuresList.add(mealDetails.getStrMeasure20());
        return measuresList;
    }
    private String getVideoIdFromUrl(String videoUrl) {
        return videoUrl.substring(videoUrl.lastIndexOf("=") + 1);
    }
}