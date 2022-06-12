package at.aau.se2.gamma.core.models.impl;

import java.io.Serializable;

/**
 * Notify about state changes, like if a closed field is detected
 */
public interface GameMapHandler extends Serializable {
    public void onClosedField(ClosedFieldDetectionData detectionData);
}
