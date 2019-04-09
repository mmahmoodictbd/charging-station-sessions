package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;

public interface ChargingSessionService {

    IdentityResponse createSession();

    void stopSession(String id);

    void deleteStoppedSessions();

}
