package com.molgenie.i2s.models;

import com.molgenie.i2s.models.common.IMixedImageRequest;

public class OsrImageRequest implements IMixedImageRequest {
	private String classifierName;
	private String imageContent;
	private String imageFilename;
	private String imageFormat;
	
	public String getClassifierName() {
		return classifierName;
	}
	public void setClassifierName(String classifierName) {
		this.classifierName = classifierName;
	}

	@Override
	public String getImageContent() {
		return imageContent;
	}
	public void setImageContent(String imageContent) {
		this.imageContent = imageContent;
	}

	@Override
	public String getImageFilename() {
		return imageFilename;
	}
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}
	
	@Override
	public String getImageFormat() {
		return imageFormat;
	}
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

}

