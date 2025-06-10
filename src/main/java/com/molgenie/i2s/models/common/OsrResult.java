package com.molgenie.i2s.models.common;

import java.util.ArrayList;

public record OsrResult( String taskId, ArrayList<Compound> compounds, ArrayList<Markush> markush, String rawData ) {
	
}