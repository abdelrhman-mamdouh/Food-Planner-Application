package com.example.foodzarella.slider.model;
public class SliderModal {
	private String title;
	private String heading;
	private final int imageDrawableId;
	private final int backgroundDrawable;

	public SliderModal(String title, String heading, int imageDrawableId, int backgroundDrawable) {
		this.title = title;
		this.heading = heading;
		this.imageDrawableId = imageDrawableId;
		this.backgroundDrawable = backgroundDrawable;
	}

	// Getters for title and heading remain the same
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	// Getters for imageDrawableId and backgroundDrawable
	public int getImageDrawableId() {
		return imageDrawableId;
	}

	public int getBackgroundDrawable() {
		return backgroundDrawable;
	}
}
