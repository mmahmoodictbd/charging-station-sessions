package com.unloadbrain.assignement.evbox.events;

import com.unloadbrain.assignement.evbox.domain.model.ChargingSession;
import org.springframework.context.ApplicationEvent;

public class ChargingSessionStartedEvent extends ApplicationEvent {

    public ChargingSessionStartedEvent(ChargingSession source) {
        super(source);
    }
}
