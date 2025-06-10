package com.molgenie.i2s.models.common;

public record ErrorResponse(ProcessState processState) {

    public ErrorResponse(String error, int errorCode, String fileName) {
        this(new ProcessState(false, errorCode, error));
    }
    
}
