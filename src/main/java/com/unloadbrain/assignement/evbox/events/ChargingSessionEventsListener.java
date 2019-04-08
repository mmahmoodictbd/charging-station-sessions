package com.unloadbrain.assignement.evbox.events;

import com.unloadbrain.assignement.evbox.service.timewheel.SessionCountTimeWheel;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChargingSessionEventsListener {

    private SessionCountTimeWheel sessionCountTimeWheel;

    public ChargingSessionEventsListener(SessionCountTimeWheel sessionCountTimeWheel) {
        this.sessionCountTimeWheel = sessionCountTimeWheel;
    }

    @EventListener
    public void handleChargingSessionStartedEvent(ChargingSessionStartedEvent chargingSessionStartedEvent) {
        sessionCountTimeWheel.increaseSessionStartedCount();
    }

    @EventListener
    public void handleChargingSessionFinishedEvent(ChargingSessionFinishedEvent chargingSessionFinishedEvent) {
        sessionCountTimeWheel.increaseSessionFinishedCount();
    }
}
