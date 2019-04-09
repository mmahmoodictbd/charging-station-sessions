package com.unloadbrain.assignement.evbox.service;

import com.unloadbrain.assignement.evbox.domain.model.ChargingSession;
import com.unloadbrain.assignement.evbox.domain.repository.ChargingSessionRepository;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import com.unloadbrain.assignement.evbox.events.ChargingSessionFinishedEvent;
import com.unloadbrain.assignement.evbox.events.ChargingSessionStartedEvent;
import com.unloadbrain.assignement.evbox.exception.ChargingSessionNotFoundException;
import com.unloadbrain.assignement.evbox.service.timewheel.SessionCountTimeWheel;
import com.unloadbrain.assignement.evbox.util.DateUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChargingSessionServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ChargingSessionRepository chargingSessionRepository;
    private LoggedInUserService loggedInUserService;
    private SessionCountTimeWheel sessionCountTimeWheel;
    private ApplicationEventPublisher applicationEventPublisher;
    private DateUtil dateUtil;

    private ChargingSessionService chargingSessionService;

    public ChargingSessionServiceTest() {

        chargingSessionRepository = mock(ChargingSessionRepository.class);
        loggedInUserService = mock(LoggedInUserService.class);
        sessionCountTimeWheel = mock(SessionCountTimeWheel.class);
        applicationEventPublisher = mock(ApplicationEventPublisher.class);
        dateUtil = mock(DateUtil.class);

        chargingSessionService = new ChargingSessionService(chargingSessionRepository,
                loggedInUserService, applicationEventPublisher, dateUtil);
    }

    @Test
    public void shouldSaveSessionInRepositoryWhenCreateSession() {

        // Given

        ArgumentCaptor<ChargingSession> argumentCaptor = ArgumentCaptor.forClass(ChargingSession.class);

        when(loggedInUserService.getLoggedInUserStationId()).thenReturn("stationId1");

        Date currentDate = new Date();
        when(dateUtil.getCurrentDate()).thenReturn(currentDate);

        // When
        chargingSessionService.createSession();

        // Then
        verify(chargingSessionRepository).save(argumentCaptor.capture());
        assertEquals("stationId1", argumentCaptor.getValue().getStationId());
        assertEquals(currentDate, argumentCaptor.getValue().getStartedAt());
    }

    @Test
    public void shouldEmitEventWhenCreateSession() {

        // Given
        ArgumentCaptor<ChargingSessionStartedEvent> argumentCaptor
                = ArgumentCaptor.forClass(ChargingSessionStartedEvent.class);

        // When
        chargingSessionService.createSession();

        // Then
        verify(applicationEventPublisher).publishEvent(argumentCaptor.capture());
        assertEquals(ChargingSessionStartedEvent.class, argumentCaptor.getValue().getClass());
    }

    @Test
    public void shouldReturnUniqueIdWhenCreateSession() {

        // Given
        doAnswer(invocationOnMock -> {
            ChargingSession chargingSession = (ChargingSession) invocationOnMock.getArguments()[0];
            chargingSession.setId("uuid1");
            return chargingSession;
        }).when(chargingSessionRepository).save(any());

        // When
        IdentityResponse identityResponse = chargingSessionService.createSession();

        // Then
        assertEquals("uuid1", identityResponse.getId());
    }

    @Test
    public void shouldThrowExceptionWhenIdNotExistWhenStoppingSession() {

        // Given
        thrown.expect(ChargingSessionNotFoundException.class);
        thrown.expectMessage("Charging session unknownSessionId not found.");

        // When
        chargingSessionService.stopSession("unknownSessionId");

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldSetEndAtFieldAndSaveSessionInRepositoryWhenStoppingSession() {

        // Given

        ArgumentCaptor<ChargingSession> argumentCaptor = ArgumentCaptor.forClass(ChargingSession.class);

        ChargingSession persistedChargingSession = new ChargingSession();
        when(chargingSessionRepository.get("sessionId")).thenReturn(persistedChargingSession);

        Date currentDate = new Date();
        when(dateUtil.getCurrentDate()).thenReturn(currentDate);

        // When
        chargingSessionService.stopSession("sessionId");

        // Then
        verify(chargingSessionRepository).save(argumentCaptor.capture());
        assertEquals(currentDate, argumentCaptor.getValue().getEndedAt());
    }

    @Test
    public void shouldEmitEventWhenStoppingSession() {

        // Given

        ArgumentCaptor<ChargingSessionFinishedEvent> argumentCaptor
                = ArgumentCaptor.forClass(ChargingSessionFinishedEvent.class);

        ChargingSession persistedChargingSession = new ChargingSession();
        when(chargingSessionRepository.get("sessionId")).thenReturn(persistedChargingSession);

        // When
        chargingSessionService.stopSession("sessionId");

        // Then
        verify(applicationEventPublisher).publishEvent(argumentCaptor.capture());
        assertEquals(ChargingSessionFinishedEvent.class, argumentCaptor.getValue().getClass());
    }

    @Test
    public void shouldInvokeDeleteAllFinishedSessionWhenDeleteStoppedSessionsGetCalled() {

        // When
        chargingSessionService.deleteStoppedSessions();

        // Then
        verify(chargingSessionRepository, times(1)).deleteAllFinishedSession();

    }
}