package com.example.foodzarella.slider.presenter;


import com.example.foodzarella.R;
import com.example.foodzarella.slider.contract.SliderContract;
import com.example.foodzarella.slider.model.SliderModal;

import java.io.InputStream;
import java.util.ArrayList;

public class SliderPresenter implements SliderContract.Presenter {

    private SliderContract.View view;

    public SliderPresenter(SliderContract.View view) {
        this.view = view;
    }

    @Override
    public void loadSliderData() {
        ArrayList<SliderModal> sliderModalArrayList = new ArrayList<>();
        sliderModalArrayList.add(new SliderModal("What will I eat today?", "Never stress about meal planning again!", "think_animation.json", R.drawable.gradient_one));
        sliderModalArrayList.add(new SliderModal("FoodZarella", "Just view categories, suggesting meals, and searching for a specific meal using many options", "foods_animation.json", R.drawable.gradient_two));
        sliderModalArrayList.add(new SliderModal("App Features", "Plan your weekly meals with ease, FoodZarella also offers a personalized favorite list feature", "mobile_animation.json", R.drawable.gradient_three));
        view.showSlider(sliderModalArrayList);
    }

    @Override
    public void onSkipClicked() {
        view.navigateToMain();
    }
}
