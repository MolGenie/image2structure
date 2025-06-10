package com.molgenie.i2s.services;

import com.molgenie.i2s.models.ClassifyImageRequest;
import com.molgenie.i2s.models.ClassifyImageResponse;
import com.molgenie.i2s.models.common.ClassificationOutcome;
import com.molgenie.i2s.models.common.ScoreOutcome;
import jakarta.inject.Singleton;

import java.util.Random;

@Singleton
public class FakeImageClassificationService implements IImageClassificationService {
	private final Random rnd = new Random();

	@Override
	public ClassifyImageResponse classifyImage(ClassifyImageRequest request) {
		if (rnd.nextBoolean()) {
			return new ClassifyImageResponse(
					new ClassificationOutcome(
							new String[]{"test-tag"},
							new ScoreOutcome[]{new ScoreOutcome(new String[]{"0.1", "0.2"})}),
					request.getClassifierName());
		} else {
			throw new ProcessingErrorException("You are not lucky to pass", rnd.nextInt(400, 599));
		}
	}
} 