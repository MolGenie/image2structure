package com.molgenie.i2s.services;

import com.molgenie.i2s.models.ClassifyImageRequest;
import com.molgenie.i2s.models.ClassifyImageResponse;
import com.molgenie.i2s.models.common.ClassificationOutcome;
import com.molgenie.i2s.models.common.ScoreOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FakeImageClassificationServiceTest {

    private FakeImageClassificationService service;

    @BeforeEach
    void setUp() {
        service = new FakeImageClassificationService();
    }

    @Test
    void classifyImage_ShouldReturnValidResponse() {
        // Arrange
        ClassifyImageRequest request = new ClassifyImageRequest();
        request.setClassifierName("test-classifier");

        // Act & Assert
        // Since the service randomly succeeds or fails, we need to try multiple times
        boolean success = false;
        boolean failure = false;
        for (int i = 0; i < 100; i++) {
            try {
                ClassifyImageResponse response = service.classifyImage(request);
                success = true;
                assertNotNull(response);
                assertEquals("test-classifier", response.classifierName);
                assertNotNull(response.classificationOutcome);
                assertNotNull(response.classificationOutcome.labels());
                assertNotNull(response.classificationOutcome.outcomes());
                assertEquals(1, response.classificationOutcome.labels().length);
                assertEquals(1, response.classificationOutcome.outcomes().length);
                assertEquals("test-tag", response.classificationOutcome.labels()[0]);
                assertEquals(2, response.classificationOutcome.outcomes()[0].scores().length);
            } catch (ProcessingErrorException e) {
                failure = true;
                assertTrue(e.getErrorCode() >= 400 && e.getErrorCode() < 600);
            }
            if (success && failure) break;
        }
        assertTrue(success, "Service should succeed at least once");
        assertTrue(failure, "Service should fail at least once");
    }

    @Test
    void classifyImage_ShouldHandleNullClassifierName() {
        // Arrange
        ClassifyImageRequest request = new ClassifyImageRequest();

        // Act & Assert
        // Since the service randomly succeeds or fails, we need to try multiple times
        boolean success = false;
        boolean failure = false;
        for (int i = 0; i < 100; i++) {
            try {
                ClassifyImageResponse response = service.classifyImage(request);
                success = true;
                assertNotNull(response);
                assertNull(response.classifierName);
            } catch (ProcessingErrorException e) {
                failure = true;
                assertTrue(e.getErrorCode() >= 400 && e.getErrorCode() < 600);
            }
            if (success && failure) break;
        }
        assertTrue(success, "Service should succeed at least once");
        assertTrue(failure, "Service should fail at least once");
    }
} 