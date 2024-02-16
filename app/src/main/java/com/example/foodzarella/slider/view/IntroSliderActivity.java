package com.example.foodzarella.slider.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.foodzarella.MainActivity;
import com.example.foodzarella.R;
import com.example.foodzarella.slider.contract.SliderContract;
import com.example.foodzarella.slider.model.SliderModal;
import com.example.foodzarella.slider.presenter.SliderPresenter;

import java.util.ArrayList;

public class IntroSliderActivity extends AppCompatActivity implements SliderContract.View {

    private SliderContract.Presenter presenter;
    private Button skip;
    private ViewPager viewPager;
    private LinearLayout dotsLL;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_slider);

        SharedPreferences preferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
        String firstTime = preferences.getString("FirstInstall", "");
        if (firstTime.equals("no")) {
            navigateToMain();
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstInstall", "no");
            editor.apply();
        }

        viewPager = findViewById(R.id.idViewPager);
        dotsLL = findViewById(R.id.idLLDots);
        presenter = new SliderPresenter(this);
        presenter.loadSliderData();
        skip = findViewById(R.id.idBtnSkip);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < dots.length; i++) {
                    if (i == position) {
                        dots[i].setTextColor(getResources().getColor(R.color.AppThem));
                    } else {
                        dots[i].setTextColor(getResources().getColor(R.color.black));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSkipClicked();
            }
        });
    }
    @Override
    public void showSlider(ArrayList<SliderModal> sliders) {
        SliderAdapter adapter = new SliderAdapter(this, sliders);
        viewPager.setAdapter(adapter);
        addDots(sliders.size(), 0);
    }

    @Override
    public void navigateToMain() {
        Intent intent = new Intent(IntroSliderActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addDots(int size, int pos) {
        dots = new TextView[size];
        dotsLL.removeAllViews();
        dotsLL.setGravity(Gravity.CENTER_HORIZONTAL);
        for (int i = 0; i < size; i++) {
            dots[i] = new TextView(this);
            dots[i].setText("●");
            dots[i].setTextSize(30);

            dots[i].setTextColor(getResources().getColor(R.color.AppThem));
            dotsLL.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[pos].setTextColor(getResources().getColor(R.color.black));
        }
    }
}
