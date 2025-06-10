package com.molgenie.i2s.services;

import com.molgenie.i2s.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class OsraCommandServiceTest {

    @Mock
    private AppProperties appProperties;

    private OsraCommandService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(appProperties.osraPath()).thenReturn(java.util.Optional.empty());
        service = new OsraCommandService(appProperties);
    }

    @Test
    void executeCommand_ShouldReturnCommandOutput() {
        // Given
        List<String> command = List.of("echo", "123");

        // When
        String result = service.executeCommand(command);

        // Then
        assertEquals("123", result);
    }

    @Test
    void executeCommand_ShouldThrowException_WhenCommandFails() {
        // Given
        List<String> command = List.of("nonexistentcommand");

        // When/Then
        assertThrows(ProcessingErrorException.class, () -> service.executeCommand(command));
    }

    @Test
    void executeCommand_ShouldThrowException_WhenCommandExitsWithNonZeroCode() {
        // Given
        String errorMessage = "Test error message";
        List<String> command = List.of("sh", "-c", "echo '" + errorMessage + "' >&2 && false");

        // When/Then
        ProcessingErrorException exception = assertThrows(ProcessingErrorException.class, 
            () -> service.executeCommand(command));
        
        // Verify both the error code and the error message
        assertEquals(500, exception.getErrorCode());
        assertEquals(errorMessage, exception.getMessage());
    }
} 