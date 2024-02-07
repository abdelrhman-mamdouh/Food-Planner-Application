package com.example.foodzarella;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class IntroSlider extends AppCompatActivity {


    SliderAdapter adapter;
    Button skip;
    int size;
    private ViewPager viewPager;
    private LinearLayout dotsLL;
    private ArrayList<SliderModal> sliderModalArrayList;
    private TextView[] dots;

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
        @Override
        public void onPageSelected(int position) {
            addDots(size, position);
            if (position == 2) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(IntroSlider.this, MainActivity.class);
                        intent.putExtra("linkss","https://hssakingamapp.blogspot.com/");
                        startActivity(intent);
                        finish();
                    }
                },1000);

            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_slider);


        SharedPreferences preferences = getSharedPreferences("PREFERENCE",MODE_PRIVATE);

        String FristTime = preferences.getString("FirstInstall","");

        if (FristTime.equals("Yes")){
            Intent intent = new Intent(IntroSlider.this,MainActivity.class);
            intent.putExtra("linkss","https://hssakingamapp.blogspot.com/");

            startActivity(intent);

        }else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("FirstInstall", "Yes");
            editor.apply();
        }
        skip=findViewById(R.id.idBtnSkip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(IntroSlider.this,MainActivity.class);
                intent.putExtra("linkss","https://hssakingamapp.blogspot.com/");
                startActivity(intent);
                finish();
            }
        });


        // initializing all our views.
        viewPager = findViewById(R.id.idViewPager);
        dotsLL = findViewById(R.id.idLLDots);

        // in below line we are creating a new array list.
        sliderModalArrayList = new ArrayList<>();

        // on below 3 lines we are adding data to our array list.
        sliderModalArrayList.add(new SliderModal("Welcome", "Thank you for installing our App", "https://images.unsplash.com/photo-1610842546881-b282c580b51d?ixid=MXwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHw5fHx8ZW58MHx8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60", R.drawable.gradient_one));
        sliderModalArrayList.add(new SliderModal("HSS Akingam", "The School that inspires you", "https://images.unsplash.com/photo-1610783131813-475d08664ef6?ixid=MXwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxMnx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60", R.drawable.gradient_two));
        sliderModalArrayList.add(new SliderModal("App Features", "Online Registration, E-Content, Attendance Monitoring, Results, School Profile, & School Events etc", "https://images.unsplash.com/photo-1610832958506-aa56368176cf?ixid=MXwxMjA3fDB8MHxlZGl0b3JpYWwtZmVlZHwxN3x8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60", R.drawable.gradient_three));

        // below line is use to add our array list to adapter class.
        adapter = new SliderAdapter(IntroSlider.this, sliderModalArrayList);

        // below line is use to set our
        // adapter to our view pager.
        viewPager.setAdapter(adapter);

        // we are storing the size of our
        // array list in a variable.
        size = sliderModalArrayList.size();

        // calling method to add dots indicator
        addDots(size, 0);

        // below line is use to call on
        // page change listener method.
        viewPager.addOnPageChangeListener(viewListener);
    }

    private void addDots(int size, int pos) {
        // inside this method we are
        // creating a new text view.
        dots = new TextView[size];

        // below line is use to remove all
        // the views from the linear layout.
        dotsLL.removeAllViews();

        // running a for loop to add
        // number of dots to our slider.
        for (int i = 0; i < size; i++) {
            // below line is use to add the
            // dots and modify its color.
            dots[i] = new TextView(this);
            dots[i].setText(".");
            dots[i].setTextSize(35);

            // below line is called when the dots are not selected.
            dots[i].setTextColor(getResources().getColor(R.color.black));
            dotsLL.addView(dots[i]);


        }
        if (dots.length > 0) {
            // this line is called when the dots
            // inside linear layout are selected
            dots[pos].setTextColor(getResources().getColor(R.color.AppThem));

        }
    }


}