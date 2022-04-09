package com.exmodify.healtrecords.database.models.events;

public interface ProgressChangeListener {
    void update(int processed, int max);
}
