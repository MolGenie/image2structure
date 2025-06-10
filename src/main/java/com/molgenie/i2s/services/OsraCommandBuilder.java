package com.molgenie.i2s.services;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OsraCommandBuilder {
	private Optional<Path> binaryPath;
	private String executableName;
	private Optional<Path> inputFileFolder;
	private Optional<String> inputFile;
	private Optional<String> chemFormat;
	private Optional<String> osrTimeout;
	
	public OsraCommandBuilder() {
		executableName = "osra";
		inputFile = Optional.empty();
		binaryPath = Optional.empty();
		inputFileFolder = Optional.empty();
		chemFormat = Optional.empty();
		osrTimeout = Optional.empty();
	}

	public OsraCommandBuilder useBinaryPath(Optional<Path> path) {
		binaryPath = path;
		return this;
	}

	public OsraCommandBuilder useInputFileFolder(Optional<Path> path) {
		inputFileFolder = path;
		return this;
	}

	public OsraCommandBuilder useInputFile(Optional<String> fileName) {
		inputFile = fileName;
		return this;
	}

	public OsraCommandBuilder useExecutableName(String fileName) {
		if (fileName == null) throw new IllegalArgumentException("OSRA executable name cannot be null");
		executableName = fileName;
		return this;
	}
	
	public OsraCommandBuilder useChemFormat(Optional<String> _chemFormat) {
		if (chemFormat == null) {
			throw new IllegalArgumentException("default format smi is used");
		} else chemFormat = _chemFormat;
		return this;
	}
	public OsraCommandBuilder useOsrTimeout(Optional<String> _osrTimeout) {
		osrTimeout = _osrTimeout;
		return this;
	}
	
	public List<String> build() {
		List<String> res = new ArrayList<>();

		//osra command
		if (binaryPath.isPresent()) {
			res.add(binaryPath.get().resolve(executableName).toAbsolutePath().toString());
		} else {
			res.add(executableName);
		}
		
		if (inputFile.isPresent()) {
			res.add("--ocr");
			res.add("oOcCnNHFsSBuUgMeEXYZRPp23456789AmThDGQi");
			if ( osrTimeout.isPresent() ) {
				res.add("--timeout");
				res.add(osrTimeout.get());
			}
			res.add("-a");
			res.add("superatom.txt");
			res.add("-l");
			res.add("spelling.txt");
			res.add("-b"); //bond length
			res.add("-g"); //resolution guess
			res.add("-p"); //confidence
			res.add("-e"); //page
			res.add("-c"); //box coordinates
			if ( chemFormat.isPresent() ) {
				res.add("-f");
				res.add(chemFormat.get());
			} else {
				res.add("-f");
				res.add("smi");
			}
			
			if (inputFileFolder.isPresent()) {
				res.add(inputFileFolder.get().resolve(inputFile.get()).toString());
			} else {
				res.add(inputFile.get());
			}
		}
		
		return res;
	}

}
