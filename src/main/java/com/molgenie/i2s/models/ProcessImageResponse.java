package com.molgenie.i2s.models;

import com.molgenie.i2s.models.common.ClassificationOutcome;
import com.molgenie.i2s.models.common.OsrResult;

public class ProcessImageResponse {
	public ClassificationOutcome classificationOutcome;
	public OsrResult osrResult;

	public ProcessImageResponse(ClassificationOutcome classificationOutcome, OsrResult osrResult) {
		this.osrResult = osrResult;
		this.classificationOutcome = classificationOutcome;
	}
	
}

