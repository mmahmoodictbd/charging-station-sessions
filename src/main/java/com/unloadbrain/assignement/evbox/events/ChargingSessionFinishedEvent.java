package com.unloadbrain.assignement.evbox.events;

import com.unloadbrain.assignement.evbox.domain.model.ChargingSession;
import org.springframework.context.ApplicationEvent;

public class ChargingSessionFinishedEvent extends ApplicationEvent {

    public ChargingSessionFinishedEvent(ChargingSession source) {
        super(source);
    }
}
