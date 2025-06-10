package com.molgenie.i2s.models.common;

public record ProcessState(boolean processed, int errorCode, String errorMsg) {}
