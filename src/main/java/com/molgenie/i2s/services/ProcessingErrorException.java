package com.molgenie.i2s.services;

public class ProcessingErrorException extends RuntimeException {
	private final int errorCode;
	private final String fileName;

	public ProcessingErrorException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.fileName = null;
	}

	public ProcessingErrorException(String message, int errorCode, String fileName) {
		super(message);
		this.errorCode = errorCode;
		this.fileName = fileName;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getFileName() {
		return fileName;
	}
}
