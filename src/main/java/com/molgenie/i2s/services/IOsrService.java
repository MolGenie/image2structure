package com.molgenie.i2s.services;

import com.molgenie.i2s.models.OsrImageRequest;
import com.molgenie.i2s.models.OsrImageResponse;

public interface IOsrService {
	OsrImageResponse performOsr(OsrImageRequest request);
}
