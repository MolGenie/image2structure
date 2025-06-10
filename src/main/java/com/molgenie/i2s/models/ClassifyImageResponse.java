package com.molgenie.i2s.models;

import com.molgenie.i2s.models.common.ClassificationOutcome;

public class ClassifyImageResponse {
	public ClassificationOutcome classificationOutcome;
	public String classifierName;

	public ClassifyImageResponse(ClassificationOutcome classificationOutcome, String classifierName) {
		this.classifierName = classifierName;
		this.classificationOutcome = classificationOutcome;
	}
}

