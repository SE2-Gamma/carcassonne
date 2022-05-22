package at.aau.se2.gamma.core.models.impl;

/**
 * Notify about state changes, like if a closed field is detected
 */
public interface GameMapHandler {
    public void onClosedField(ClosedFieldDetectionData detectionData);
}
