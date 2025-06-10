package com.molgenie.i2s.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class OsraCommandBuilderTest {

	@TempDir
	Path tempDir;

	private OsraCommandBuilder builder;

	@BeforeEach
	void setUp() {
		builder = new OsraCommandBuilder();
	}

	@Test
	void buildCommand_WithDefaults() {
		// Act
		var command = builder.build();
		// Assert
		var expectedCommand = "osra";
		assertEquals(1, command.size());
		assertEquals(expectedCommand, command.getFirst());
	}

	@Test
	void buildCommand_WithDifferentBinaries() {
		// Arrange
		var expectedPath=Paths.get("/");
		builder.useExecutableName("osra-test");
		builder.useBinaryPath(Optional.of(expectedPath));

		// Act
		var command = builder.build();

		// Assert
		var expectedCommand = expectedPath.toAbsolutePath().resolve("osra-test").toString();
		assertEquals(1, command.size());
		assertEquals(expectedCommand, command.getFirst());
	}

	@Test
	void buildCommand_WithRelativePathAndFakeBinary() {
		// Arrange
		var expectedPath=Paths.get("bin");
		builder.useExecutableName("osra-fake.sh");
		builder.useBinaryPath(Optional.of(expectedPath));

		// Act
		var command = builder.build();

		// Assert
		var expectedCommand = expectedPath.toAbsolutePath().resolve("osra-fake.sh").toString();
		assertEquals(1, command.size());
		assertEquals(expectedCommand, command.getFirst());
	}

	@Test
	void buildCommand_WithBinaryPath_ShouldIncludePath() {
		// Arrange
		Path binaryPath = tempDir.resolve("bin");
		String executableName = "osra";
		builder.useBinaryPath(Optional.of(binaryPath));

		// Act
		var command = builder.build();

		// Assert
		assertTrue(command.contains(binaryPath.resolve(executableName).toString()));
	}

	@Test
	void buildCommand_WithInputFile_ShouldIncludeInputFile() {
		// Arrange
		String inputFile = "test.png";
		builder.useInputFile(Optional.of(inputFile));

		// Act
		var command = builder.build();

		// Assert
		//assertTrue(command.contains("-i"));
		assertTrue(command.contains(inputFile));
	}

	@Test
	void buildCommand_WithInputFileAndFolder_ShouldIncludeFullPath() {
		// Arrange
		Path inputFolder = tempDir.resolve("input");
		String inputFile = "test.png";
		builder.useInputFileFolder(Optional.of(inputFolder));
		builder.useInputFile(Optional.of(inputFile));

		// Act
		var command = builder.build();

		// Assert
		//assertTrue(command.contains("-i" ));
		assertTrue(command.contains(inputFolder.resolve(inputFile).toAbsolutePath().toString()));
	}
} 