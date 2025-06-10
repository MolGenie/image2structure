package com.molgenie.i2s.models;

import com.molgenie.i2s.models.common.IMixedImageRequest;

public class ClassifyImageRequest implements IMixedImageRequest {
	private String imageContent;
	private String imageFilename;
	private String classifierName;

	@Override
	public String getImageContent() {
		return imageContent;
	}
	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}

	public String getClassifierName() {
		return classifierName;
	}
	public void setClassifierName(String classifierName) {
		this.classifierName = classifierName;
	}
	@Override
	public String getImageFilename() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getImageFormat() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
