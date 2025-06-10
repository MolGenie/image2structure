package com.molgenie.i2s.config;

import java.nio.file.Path;
import java.util.Optional;

public interface IOsraSettings {
	public Optional<Path> osraPath();
	public String osraExecutable();
	public String osrConfidence();
	public String osrTimeout();
	public String chemFormat();
}
