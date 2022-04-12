package com.exmodify.healtrecords.database.models.events;

/**
 * Progress change event listener
 * The {@link #update(int, int)} function is invoked every time the there's new progress in the given process.
 */
public interface ProgressChangeListener {
    /**
     * This is invoked when the specific process has progressed
     * @param processed the current progress of the specific process
     * @param max the final step of the specific process
     */
    void update(int processed, int max);
}
