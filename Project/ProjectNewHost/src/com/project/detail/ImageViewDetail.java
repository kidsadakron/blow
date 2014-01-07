package com.project.detail;

import android.widget.ImageView;

public class ImageViewDetail {
	private String url;
	private ImageView imgView;
	public ImageViewDetail(String url, ImageView imgView) {
		this.imgView = imgView;
		this.url = url;


	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ImageView getImgView() {
		return imgView;
	}

	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}
}
