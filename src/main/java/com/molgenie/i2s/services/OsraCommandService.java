package com.molgenie.i2s.services;

import com.molgenie.i2s.config.IOsraSettings;
import com.molgenie.i2s.models.OsrImageRequest;
import com.molgenie.i2s.models.OsrImageResponse;
import com.molgenie.i2s.models.common.OsrResult;
import com.molgenie.i2s.models.common.ChemistryChecks;
import com.molgenie.i2s.models.common.Compound;
import com.molgenie.i2s.models.common.Markush;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Singleton
public class OsraCommandService implements IOsrService {
	private static final Logger log = LoggerFactory.getLogger(OsraCommandService.class);
	private final IOsraSettings settings;

	@Inject
	public OsraCommandService(IOsraSettings settings) {
		this.settings = settings;
	}

	public OsrImageResponse performOsr(OsrImageRequest request) {
		var inputFile = request.getImageFilename();
		if (inputFile == null || inputFile.isBlank()) {
			inputFile = createFile(request.getImageContent());
		}

		var cmd = new OsraCommandBuilder()
				.useExecutableName(settings.osraExecutable())
				.useBinaryPath(settings.osraPath())
				.useInputFile(Optional.of(inputFile))
				.useChemFormat(Optional.of(settings.chemFormat()))
				.useOsrTimeout(Optional.of(settings.osrTimeout()))
				.build();
		
		var stdOutput = executeCommand(cmd);
		return new OsrImageResponse( buildOsrResponse(stdOutput), inputFile );
	}

	private OsrResult buildOsrResponse( String stdOutput ) {
		//parse the output and build the response object
		ArrayList<Compound> compounds = new java.util.ArrayList();
		ArrayList<Markush> markush = new java.util.ArrayList();
		
		if (settings.chemFormat().equals("smi")) {
			String[] molecules = stdOutput.split("\n");
			if ( molecules.length >0 ) {
				//System.out.println("found compounds:"+molecules.length);
				for ( int i=0; i<molecules.length; i++) {
					Compound compound = new Compound();
					Markush compoundClass = new Markush();
					String[] splitOSR = molecules[i].split(" ");
					String smiles = splitOSR[0];
					String idcode = ChemistryChecks.checkMolecule(smiles);
					if ( idcode == null ) continue;
					float confidence = Float.valueOf( splitOSR[3] );
					try {
						if ( smiles.contains("*") ) {
							compoundClass.setSmiles( smiles );
							//System.out.println(smiles);
							compoundClass.setResolution( Integer.valueOf( splitOSR[2] ));
							compoundClass.setConfidence( confidence );
							compoundClass.setPage( Integer.valueOf( splitOSR[4] ));
							compoundClass.setPosition( splitOSR[5] );
							compoundClass.setIDCode( idcode );
						} else {
							compound.setSmiles( smiles );
							//System.out.println(smiles);
							compound.setResolution( Integer.valueOf( splitOSR[2] ));
							compound.setConfidence( confidence );
							compound.setPage( Integer.valueOf( splitOSR[4] ));
							compound.setPosition( splitOSR[5] );
							compound.setIDCode( idcode );
						}
						float trustedConfidence = Float.valueOf( settings.osrConfidence());
						
						if ( confidence > trustedConfidence ) {
							if ( smiles.contains("*") && compoundClass != null ) markush.add(compoundClass);
							else if ( compound != null ) compounds.add(compound);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
		return new OsrResult( null, compounds, markush, stdOutput );
	}

	private String createFile(String imageContent) {
		try {
			byte[] decodedImage = java.util.Base64.getDecoder().decode(imageContent);
			Path tempFile = java.nio.file.Files.createTempFile("i2s_base64_income_", "");
			java.nio.file.Files.write(tempFile, decodedImage);
			return tempFile.toAbsolutePath().toString();
		} catch (IllegalArgumentException e) {
			log.error("Failed to decode Base64 image content", e);
			throw new ProcessingErrorException("Invalid Base64 image content", 400);
		} catch (IOException e) {
			log.error("Failed to create temporary file", e);
			throw new ProcessingErrorException("Failed to create temporary file: " + e.getMessage(), 500);
		}
	}

	protected String executeCommand(java.util.List<String> cmd) {
		log.info("Executing command: {}", String.join(" ", cmd));
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(cmd);
			processBuilder.redirectErrorStream(false); // Keep stdout and stderr separate
			Process process = processBuilder.start();

			// Read stdout
			StringBuilder stdout = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					stdout.append(line).append("\n");
				}
			}

			// Read stderr
			StringBuilder stderr = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
				String line;
				while ((line = reader.readLine()) != null) {
					stderr.append(line).append("\n");
				}
			}

			// Wait for the process to complete
			int exitCode = process.waitFor();

			if ((exitCode == 0)||(exitCode == 15)) {
				return stdout.toString().trim();
			} else {
				String errorMessage = stderr.toString().trim();
				log.error("OSRA execution failed with exit code {}: {}", exitCode, errorMessage);
				throw new ProcessingErrorException(errorMessage, 500);
			}
		} catch (IOException e) {
			log.error("Failed to execute OSRA command", e);
			throw new ProcessingErrorException("Failed to execute OSRA: " + e.getMessage(), 500);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("OSRA command execution was interrupted", e);
			throw new ProcessingErrorException("OSRA command execution was interrupted", 500);
		}
	}
	
} 