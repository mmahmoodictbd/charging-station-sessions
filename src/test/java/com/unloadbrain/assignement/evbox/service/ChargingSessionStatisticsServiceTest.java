package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.service.timewheel.SessionCountTimeWheel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChargingSessionStatisticsServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SessionCountTimeWheel sessionCountTimeWheel;
    private ChargingSessionStatisticsService chargingSessionStatisticsService;

    public ChargingSessionStatisticsServiceTest() {
        sessionCountTimeWheel = mock(SessionCountTimeWheel.class);
        chargingSessionStatisticsService = new ChargingSessionStatisticsService(sessionCountTimeWheel);
    }

    @Test
    public void shouldReturnSummeryFromTimeWheel() {

        // Given

        when(sessionCountTimeWheel.getTotalSessionsStartedInLast60Seconds()).thenReturn(10L);
        when(sessionCountTimeWheel.getTotalFinishedSessionsInLast60Seconds()).thenReturn(5L);
        when(sessionCountTimeWheel.getTotalOngoingSessionsInLast60Seconds()).thenReturn(5L);


        // When
        ChargingSessionsSummeryResponse summeryResponse = chargingSessionStatisticsService.getSessionSummery();

        // Then
        assertEquals(10, summeryResponse.getStartedCount());
        assertEquals(5, summeryResponse.getStoppedCount());
        assertEquals(5, summeryResponse.getTotalCount());
    }

    @Test
    public void shouldReturnStartedSummeryFromTimeWheel() {

        // Given
        when(sessionCountTimeWheel.getTotalSessionsStartedInLast60Seconds()).thenReturn(10L);

        // When
        ChargingStartedSessionsSummeryResponse summeryResponse = chargingSessionStatisticsService.getStartedSessionSummery();

        // Then
        assertEquals(10, summeryResponse.getStartedCount());
    }

    @Test
    public void shouldReturnFinishedSummeryFromTimeWheel() {

        // Given
        when(sessionCountTimeWheel.getTotalFinishedSessionsInLast60Seconds()).thenReturn(5L);

        // When
        ChargingStoppedSessionsSummeryResponse summeryResponse = chargingSessionStatisticsService.getStoppedSessionSummery();

        // Then
        assertEquals(5, summeryResponse.getStoppedCount());
    }


}