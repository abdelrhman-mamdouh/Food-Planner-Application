package com.example.foodzarella.slider.contract;

import com.example.foodzarella.slider.model.SliderModal;

import java.util.ArrayList;

public interface SliderContract {
    interface View {
        void showSlider(ArrayList<SliderModal> sliders);
        void navigateToMain();
    }

    interface Presenter {
        void loadSliderData();
        void onSkipClicked();
    }
}