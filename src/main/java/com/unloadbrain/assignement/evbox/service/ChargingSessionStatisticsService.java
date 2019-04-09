package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;

public interface ChargingSessionStatisticsService {

    ChargingSessionsSummeryResponse getSessionSummery();

    ChargingStartedSessionsSummeryResponse getStartedSessionSummery();

    ChargingStoppedSessionsSummeryResponse getStoppedSessionSummery();
}
