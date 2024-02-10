package com.example.foodzarella.slider.model;


public class SliderModal {
	private String title;
	private String heading;
	private final String animationFileName;
	private final int backgroundDrawable;

	public SliderModal(String title, String heading, String animationFileName, int backgroundDrawable) {
		this.title = title;
		this.heading = heading;
		this.animationFileName = animationFileName;
		this.backgroundDrawable = backgroundDrawable;
	}

	public String getTitle() {
		return title;
	}

	public String getHeading() {
		return heading;
	}

	public String getAnimationFileName() {
		return animationFileName;
	}

	public int getBackgroundDrawable() {
		return backgroundDrawable;
	}
}
