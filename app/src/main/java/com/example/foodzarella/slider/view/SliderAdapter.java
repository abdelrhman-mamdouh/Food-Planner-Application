package com.example.foodzarella.slider.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.example.foodzarella.R;
import com.example.foodzarella.slider.model.SliderModal;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class SliderAdapter extends PagerAdapter {
	private LayoutInflater layoutInflater;
	private Context context;
	private ArrayList<SliderModal> sliderModalArrayList;

	public SliderAdapter(Context context, ArrayList<SliderModal> sliderModalArrayList) {
		this.context = context;
		this.sliderModalArrayList = sliderModalArrayList;
	}

	@Override
	public int getCount() {
		return sliderModalArrayList.size();
	}

	@Override
	public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
		return view == (RelativeLayout) object;
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.slider_layout, container, false);
		GifImageView gifImageView = view.findViewById(R.id.idIV);
		TextView titleTV = view.findViewById(R.id.idTVtitle);
		TextView headingTV = view.findViewById(R.id.idTVheading);
		RelativeLayout sliderRL = view.findViewById(R.id.idRLSlider);
		SliderModal modal = sliderModalArrayList.get(position);
		titleTV.setText(modal.getTitle());
		headingTV.setText(modal.getHeading());
		gifImageView.setImageResource(modal.getImageDrawableId());
		sliderRL.setBackgroundResource(modal.getBackgroundDrawable());
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((RelativeLayout) object);
	}
}
