package com.molgenie.i2s.config;

import java.nio.file.Path;
import java.util.Optional;

public record AppProperties(
		int listeningPort,
		int maxThreadCount,
		Optional<Path> osraPath,
		String osraExecutable,
		String baseApiPath,
		String osrConfidence,
		String osrTimeout,
		String chemFormat
		)
		implements IJettySettings, IOsraSettings, IApiSettings {

}
