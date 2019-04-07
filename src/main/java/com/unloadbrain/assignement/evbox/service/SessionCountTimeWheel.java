package com.unloadbrain.assignement.evbox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class SessionCountTimeWheel extends TimerTask {

    private static SessionCountTimeWheel timeWheel = new SessionCountTimeWheel();

    private WheelSpoke head;
    private WheelSpoke end;

    private long last60SecondsOngoingSessionCount;
    private long last60SecondsStartedSessionCount;
    private long last60SecondsFinishedSessionCount;

    private SessionCountTimeWheel() {

        head = new WheelSpoke(new SessionCount(0));
        WheelSpoke currentPointer = head;
        for (int i = 1; i < 60; i++) {
            WheelSpoke spoke = new WheelSpoke(new SessionCount(0));
            currentPointer.setNext(spoke);
            currentPointer = spoke;
        }
        end = currentPointer;

        Timer timer = new Timer();
        timer.schedule(this, 0, 5000);
    }

    public static SessionCountTimeWheel instance() {
        return timeWheel;
    }

    @Override
    public void run() {
        spinWheel();
    }

    public void spinWheel() {

        System.out.println("spinning wheel " + System.currentTimeMillis());

        updateStartedFinishedOngoingSessionCount();

        head = head.getNext();
        long ongoingSessionCount = end.getData().getOngoingSessionCount()
                + end.getData().getStartedSessionCount()
                - end.getData().getEndedSessionCount();
        WheelSpoke spoke = new WheelSpoke(new SessionCount(ongoingSessionCount));
        end.setNext(spoke);
        end = spoke;

        print();
    }

    private void updateStartedFinishedOngoingSessionCount() {

        int startedSessionCount = 0;
        int finishedSessionCount = 0;
        WheelSpoke cur = head;
        while (cur != null) {
            startedSessionCount += cur.getData().getStartedSessionCount();
            finishedSessionCount += cur.getData().getEndedSessionCount();
            cur = cur.getNext();
        }

        last60SecondsStartedSessionCount = startedSessionCount;
        last60SecondsFinishedSessionCount = finishedSessionCount;

        last60SecondsOngoingSessionCount = head.getData().getOngoingSessionCount()
                + last60SecondsStartedSessionCount
                - last60SecondsFinishedSessionCount;

    }

    public void increaseSessionStartedCount() {
        end.getData().increaseStartedSessionCount();
    }

    public void increaseSessionEndedCount() {
        end.getData().increaseEndedSessionCount();
    }

    public long getTotalOngoingSessionsInLast60Seconds() {
        return last60SecondsOngoingSessionCount;
    }

    public long getTotalSessionsStartedInLast60Seconds() {
        return last60SecondsStartedSessionCount;
    }

    public long getTotalFinishedSessionsInLast60Seconds() {
        return last60SecondsFinishedSessionCount;
    }

    private void print() {
        List<WheelSpoke> spokes = new ArrayList<>(60);
        WheelSpoke cur = head;
        while (cur != null) {
            spokes.add(cur);
            cur = cur.getNext();
        }
        System.out.println("spokes = " + spokes);
        System.out.println("last60SecondsStartedSessionCount = " + last60SecondsStartedSessionCount);
        System.out.println("last60SecondsFinishedSessionCount = " + last60SecondsFinishedSessionCount);
        System.out.println("last60SecondsOngoingSessionCount = " + last60SecondsOngoingSessionCount);

    }

    public static class WheelSpoke {

        private SessionCount data;
        private WheelSpoke next;

        public WheelSpoke(SessionCount data) {
            this.data = data;
        }

        public SessionCount getData() {
            return data;
        }

        public WheelSpoke getNext() {
            return next;
        }

        public void setNext(WheelSpoke next) {
            this.next = next;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    public class SessionCount {

        private long ongoingSessionCount;
        private final AtomicLong startedSessionCount = new AtomicLong(0);
        private final AtomicLong endedSessionCount = new AtomicLong(0);

        public SessionCount(long ongoingSessionCount) {
            this.ongoingSessionCount = ongoingSessionCount;
        }

        public void increaseStartedSessionCount() {
            startedSessionCount.incrementAndGet();
        }

        public void increaseEndedSessionCount() {
            endedSessionCount.incrementAndGet();
        }

        public long getStartedSessionCount() {
            return startedSessionCount.get();
        }

        public long getEndedSessionCount() {
            return endedSessionCount.get();
        }

        public long getOngoingSessionCount() {
            return ongoingSessionCount;
        }

        @Override
        public String toString() {
            return startedSessionCount.get() + " " + endedSessionCount.get();
        }
    }
}
