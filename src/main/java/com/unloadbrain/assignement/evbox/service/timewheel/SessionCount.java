package com.unloadbrain.assignement.evbox.service.timewheel;

import java.util.concurrent.atomic.AtomicLong;

public class SessionCount {

    private long ongoingSessionCount;
    private final AtomicLong startedSessionCount = new AtomicLong(0);
    private final AtomicLong finishedSessionCount = new AtomicLong(0);

    public SessionCount(long ongoingSessionCount) {
        this.ongoingSessionCount = ongoingSessionCount;
    }

    public void increaseStartedSessionCount() {
        startedSessionCount.incrementAndGet();
    }

    public void increaseFinishedSessionCount() {
        finishedSessionCount.incrementAndGet();
    }

    public long getStartedSessionCount() {
        return startedSessionCount.get();
    }

    public long getFinishedSessionCount() {
        return finishedSessionCount.get();
    }

    public long getOngoingSessionCount() {
        return ongoingSessionCount;
    }

    @Override
    public String toString() {
        return startedSessionCount.get() + " " + finishedSessionCount.get();
    }
}