package com.unloadbrain.assignement.evbox.service.timewheel;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Time wheel data structure to hold session count over time.
 */
public class SessionCountTimeWheel {

    private final static int WHEEL_SIZE = 60;
    private final static long WHEEL_ROTATION_SPEED = 1000;

    private static SessionCountTimeWheel instance = new SessionCountTimeWheel();

    private WheelSpoke head;
    private WheelSpoke end;
    private TimerTask timeWheelHeart;

    /**
     * Only initialized using instance()
     */
    private SessionCountTimeWheel() {

        head = new WheelSpoke(new SessionCount(0));
        WheelSpoke currentPointer = head;
        for (int i = 1; i < WHEEL_SIZE; i++) {
            WheelSpoke spoke = new WheelSpoke(new SessionCount(0));
            currentPointer.setNext(spoke);
            currentPointer = spoke;
        }
        end = currentPointer;

        timeWheelHeart = new TimerTask() {
            @Override
            public void run() {
                spinWheel();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timeWheelHeart, 0, WHEEL_ROTATION_SPEED);
    }

    /**
     * Singleton instance of SessionCountTimeWheel class.
     *
     * @return
     */
    public static SessionCountTimeWheel instance() {
        return instance;
    }

    private void spinWheel() {

        head = head.getNext();
        long ongoingSessionCount = end.getData().getOngoingSessionCount()
                + end.getData().getStartedSessionCount()
                - end.getData().getFinishedSessionCount();
        WheelSpoke spoke = new WheelSpoke(new SessionCount(ongoingSessionCount));
        end.setNext(spoke);
        end = spoke;
    }

    /**
     * Increase started session count in tine wheel
     */
    public void increaseSessionStartedCount() {
        end.getData().increaseStartedSessionCount();
    }

    /**
     * Increase finished session count in tine wheel
     */
    public void increaseSessionFinishedCount() {
        end.getData().increaseFinishedSessionCount();
    }

    /**
     * Return total number of ongoing session in last minute
     *
     * @return
     */
    public long getTotalOngoingSessionsInLast60Seconds() {

        return head.getData().getOngoingSessionCount()
                + getTotalSessionsStartedInLast60Seconds()
                - getTotalFinishedSessionsInLast60Seconds();
    }

    /**
     * Return total number of started session in last minute
     *
     * @return
     */
    public long getTotalSessionsStartedInLast60Seconds() {

        int startedSessionCount = 0;
        WheelSpoke currentPointer = head;
        while (currentPointer != null) {
            startedSessionCount += currentPointer.getData().getStartedSessionCount();
            currentPointer = currentPointer.getNext();
        }

        return startedSessionCount;
    }

    /**
     * Return total number of finished session in last minute
     * @return
     */
    public long getTotalFinishedSessionsInLast60Seconds() {

        int finishedSessionCount = 0;
        WheelSpoke currentPointer = head;
        while (currentPointer != null) {
            finishedSessionCount += currentPointer.getData().getFinishedSessionCount();
            currentPointer = currentPointer.getNext();
        }

        return finishedSessionCount;
    }

}
