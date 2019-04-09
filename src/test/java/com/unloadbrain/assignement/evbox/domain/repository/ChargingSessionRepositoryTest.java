package com.unloadbrain.assignement.evbox.domain.repository;

import com.unloadbrain.assignement.evbox.domain.model.ChargingSession;
import com.unloadbrain.assignement.evbox.util.UuidUtil;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChargingSessionRepositoryTest {

    private UuidUtil uuidUtilMock;
    private ChargingSessionRepository repository;

    public ChargingSessionRepositoryTest() {
        uuidUtilMock = mock(UuidUtil.class);
        repository = new ChargingSessionRepository(uuidUtilMock);
    }

    @Test
    public void shouldSaveNewStartedSessionAndHaveUniqueId() {

        // Given
        ChargingSession chargingSession = new ChargingSession();
        when(uuidUtilMock.getRandomUuid()).thenReturn("uuid1");

        // When
        repository.save(chargingSession);

        // Then
        assertEquals("uuid1", repository.get("uuid1").getId());
    }

    @Test
    public void alreadyPersistedSessionShouldJustSave() {

        // Given
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setId("uuid1");

        // When
        repository.save(chargingSession);

        // Then
        assertEquals("uuid1", repository.get("uuid1").getId());
    }

    @Test
    public void finishedSessionShouldNotExist() {

        // Given
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setId("uuid1");
        chargingSession.setEndedAt(new Date());

        // When
        repository.save(chargingSession);

        // Then
        assertNull(repository.get("uuid1"));
    }

    @Test
    public void shouldReturnStoppedSession() {

        // Given
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setId("uuid1");
        chargingSession.setEndedAt(new Date());
        repository.save(chargingSession);

        // When
        ChargingSession persistedChargingSession = repository.getStoppedSession("uuid1");

        // Then
        assertNotNull(persistedChargingSession);
    }

    @Test
    public void deleteAllFinishedSessionShouldFlushAllStoppedSessionCollection() {

        // Given
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setId("uuid1");
        chargingSession.setEndedAt(new Date());
        repository.save(chargingSession);

        // When
        repository.deleteAllFinishedSession();

        // Then
        assertNull(repository.getStoppedSession("uuid1"));
    }
}