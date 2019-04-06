package com.unloadbrain.assignement.evbox.domain.repository;

import com.unloadbrain.assignement.evbox.domain.model.ChargingSession;
import com.unloadbrain.assignement.evbox.util.UuidUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChargingSessionRepository {

    private Map<String, ChargingSession> startedSessions = new ConcurrentHashMap<>();
    private Map<String, ChargingSession> stoppedSessions = new ConcurrentHashMap<>();
    private UuidUtil uuidUtil;

    public ChargingSessionRepository(UuidUtil uuidUtil) {
        this.uuidUtil = uuidUtil;
    }

    public ChargingSession save(ChargingSession chargingSession) {

        if (chargingSession.getId() == null) {
            String id = uuidUtil.getRandomUuid();
            chargingSession.setId(id);
            startedSessions.put(id, chargingSession);
        } else {
            startedSessions.remove(chargingSession);
            stoppedSessions.put(chargingSession.getId(), chargingSession);
        }

        return chargingSession;
    }

    public ChargingSession get(String id) {
        return startedSessions.get(id);
    }

    public void deleteAllFinishedSession() {
        stoppedSessions = new ConcurrentHashMap<>();
    }

}
