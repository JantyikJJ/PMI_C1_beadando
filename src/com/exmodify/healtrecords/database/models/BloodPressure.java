package com.exmodify.healtrecords.database.models;

/**
 * BloodPressure enum which can be various depending on the different systolic and diastolic properties
 */
public enum BloodPressure {
    /**
     * Normal blood pressure level
     */
    Normal,
    /**
     * Blood pressures with high systolic
     */
    HighSystolic,
    /**
     * Blood pressures with high diastolic
     */
    HighDiastolic,
    /**
     * Blood pressures with low systolic
     */
    LowSystolic,
    /**
     * Blood pressures with low diastolic
     */
    LowDiastolic
}
