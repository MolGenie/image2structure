package com.molgenie.i2s.models.common;

public record ClassificationOutcome(String[] labels, ScoreOutcome[] outcomes) {
}
