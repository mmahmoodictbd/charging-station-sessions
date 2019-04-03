package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.dto.request.ChargingSessionCreateRequest;
import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ChargingSessionService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ChargingSessionService() {
    }

    public IdentityResponse createSession(ChargingSessionCreateRequest chargingSessionCreateRequest) {
        return null;
    }

    public void stopSession(String id) {
    }

    public ChargingSessionsSummeryResponse getSessionSummery() {
        return null;
    }

    public void deleteStoppedChargingSessions() {
    }

    public ChargingStoppedSessionsSummeryResponse getStoppedSessionSummery() {
        return null;
    }

    public ChargingStartedSessionsSummeryResponse getStartedSessionSummery() {
        return null;
    }
}
