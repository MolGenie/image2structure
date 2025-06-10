package com.molgenie.i2s.services;

import com.molgenie.i2s.models.ClassifyImageRequest;
import com.molgenie.i2s.models.ClassifyImageResponse;

public interface IImageClassificationService {
    ClassifyImageResponse classifyImage(ClassifyImageRequest request);
}
