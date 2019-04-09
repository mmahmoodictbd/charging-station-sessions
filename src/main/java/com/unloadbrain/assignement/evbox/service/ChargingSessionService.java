package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.domain.model.ChargingSession;
import com.unloadbrain.assignement.evbox.domain.repository.ChargingSessionRepository;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import com.unloadbrain.assignement.evbox.events.ChargingSessionFinishedEvent;
import com.unloadbrain.assignement.evbox.events.ChargingSessionStartedEvent;
import com.unloadbrain.assignement.evbox.exception.ChargingSessionNotFoundException;
import com.unloadbrain.assignement.evbox.util.DateUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ChargingSessionService {

    private ChargingSessionRepository chargingSessionRepository;
    private LoggedInUserService loggedInUserService;
    private ApplicationEventPublisher applicationEventPublisher;
    private DateUtil dateUtil;

    public ChargingSessionService(ChargingSessionRepository chargingSessionRepository,
                                  LoggedInUserService loggedInUserService,
                                  ApplicationEventPublisher applicationEventPublisher,
                                  DateUtil dateUtil) {
        this.chargingSessionRepository = chargingSessionRepository;
        this.loggedInUserService = loggedInUserService;
        this.applicationEventPublisher = applicationEventPublisher;
        this.dateUtil = dateUtil;
    }

    public IdentityResponse createSession() {

        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStationId(loggedInUserService.getLoggedInUserStationId());
        chargingSession.setStartedAt(dateUtil.getCurrentDate());
        chargingSessionRepository.save(chargingSession);

        applicationEventPublisher.publishEvent(new ChargingSessionStartedEvent(chargingSession));

        return new IdentityResponse(chargingSession.getId());
    }

    public void stopSession(String id) {

        ChargingSession chargingSession = chargingSessionRepository.get(id);
        if (chargingSession == null) {
            throw new ChargingSessionNotFoundException(String.format("Charging session %s not found.", id));
        }

        chargingSession.setEndedAt(dateUtil.getCurrentDate());
        chargingSessionRepository.save(chargingSession);

        applicationEventPublisher.publishEvent(new ChargingSessionFinishedEvent(chargingSession));
    }

    public void deleteStoppedSessions() {
        chargingSessionRepository.deleteAllFinishedSession();
    }

}
