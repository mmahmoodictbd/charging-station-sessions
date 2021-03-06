package com.unloadbrain.assignement.evbox.controller;

import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import com.unloadbrain.assignement.evbox.service.ChargingSessionService;
import com.unloadbrain.assignement.evbox.service.ChargingSessionStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class ChargingSessionController {

    private final ChargingSessionService chargingSessionService;
    private final ChargingSessionStatisticsService chargingSessionStatisticsService;

    public ChargingSessionController(ChargingSessionService chargingSessionService,
                                     ChargingSessionStatisticsService chargingSessionStatisticsService) {
        this.chargingSessionService = chargingSessionService;
        this.chargingSessionStatisticsService = chargingSessionStatisticsService;
    }

    @PostMapping("/chargingSession")
    public ResponseEntity<IdentityResponse> createChargingSession() {
        return new ResponseEntity(chargingSessionService.createSession(), HttpStatus.CREATED);
    }

    @PutMapping("/chargingSession/{id}")
    public ResponseEntity<Void> stopChargingSession(@PathVariable String id) {

        chargingSessionService.stopSession(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/chargingSessions")
    public ResponseEntity<Void> deleteStoppedChargingSessions() {
        chargingSessionService.deleteStoppedSessions();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/chargingSessions")
    public ChargingSessionsSummeryResponse getSessionSummery() {
        return chargingSessionStatisticsService.getSessionSummery();
    }

    @GetMapping("/chargingSessions/started")
    public ChargingStartedSessionsSummeryResponse getStartedSessionSummery() {
        return chargingSessionStatisticsService.getStartedSessionSummery();
    }

    @GetMapping("/chargingSessions/stopped")
    public ChargingStoppedSessionsSummeryResponse getStoppedSessionSummery() {
        return chargingSessionStatisticsService.getStoppedSessionSummery();
    }


}
