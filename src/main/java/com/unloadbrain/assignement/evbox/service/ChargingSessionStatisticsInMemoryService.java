package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.service.timewheel.SessionCountTimeWheel;
import org.springframework.stereotype.Service;

@Service
public class ChargingSessionStatisticsInMemoryService implements ChargingSessionStatisticsService {

    private SessionCountTimeWheel sessionCountTimeWheel;

    public ChargingSessionStatisticsInMemoryService(SessionCountTimeWheel sessionCountTimeWheel) {
        this.sessionCountTimeWheel = sessionCountTimeWheel;
    }
    
    public ChargingSessionsSummeryResponse getSessionSummery() {
        return ChargingSessionsSummeryResponse.builder()
                .startedCount(sessionCountTimeWheel.getTotalSessionsStartedInLast60Seconds())
                .stoppedCount(sessionCountTimeWheel.getTotalFinishedSessionsInLast60Seconds())
                .totalCount(sessionCountTimeWheel.getTotalOngoingSessionsInLast60Seconds())
                .build();
    }

    public ChargingStartedSessionsSummeryResponse getStartedSessionSummery() {
        return ChargingStartedSessionsSummeryResponse.builder()
                .startedCount(sessionCountTimeWheel.getTotalSessionsStartedInLast60Seconds())
                .build();
    }

    public ChargingStoppedSessionsSummeryResponse getStoppedSessionSummery() {
        return ChargingStoppedSessionsSummeryResponse.builder()
                .stoppedCount(sessionCountTimeWheel.getTotalFinishedSessionsInLast60Seconds())
                .build();
    }

}
