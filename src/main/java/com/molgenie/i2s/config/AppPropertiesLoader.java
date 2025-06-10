package com.molgenie.i2s.config;

import com.molgenie.i2s.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;


public final class AppPropertiesLoader {
	private static final Logger log = LoggerFactory.getLogger(AppPropertiesLoader.class);
	private static final String DEFAULT_PROPERTIES_FILE = "i2s.default.properties";
	private static final String PROPERTIES_FILE = "config/i2s.properties";

	public static AppProperties load() {
		var defaultProperties = new Properties();
		try (var input = Main.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE)) {
			if (input == null) {
				throw new RuntimeException("Cannot load default properties from resources: " + DEFAULT_PROPERTIES_FILE);
			}
			defaultProperties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load default properties from resources: " + DEFAULT_PROPERTIES_FILE, e);
		}

		Properties appProps = new Properties(defaultProperties);
		var absolutePropertyFilePath = Path.of(PROPERTIES_FILE).toAbsolutePath().toString();
		try {
			appProps.load(new FileInputStream(absolutePropertyFilePath));
		} catch (IOException e) {
			log.warn("Cannot load application properties from {}", absolutePropertyFilePath, e);
		}

		// Convert properties to AppProperties object
		int port = Integer.parseInt(appProps.getProperty("port"));
		int maxThreads = Integer.parseInt(appProps.getProperty("maxThreads"));
		
		var osraExecFolder = appProps.getProperty("com.molgenie.img2struct.osr.OSRController.osrProgExecPath");
		Optional<Path> osraPath = Optional.empty();
		if (osraExecFolder != null) {
			osraPath = Optional.of(Path.of(osraExecFolder));
		}
		String osraExecutable = appProps.getProperty("osraExecutable");
		if (osraExecutable == null) osraExecutable = "osra";
		String baseApiPath = appProps.getProperty("baseApiPath");
		String osrConfidence = appProps.getProperty("osrConfidence");
		String osrTimeout = appProps.getProperty("osrTimeout");
		String chemFormat = appProps.getProperty("chemFormat");
		
		return new AppProperties(port, maxThreads, osraPath, osraExecutable, baseApiPath, osrConfidence, osrTimeout, chemFormat );
	}
}
