package com.unloadbrain.assignement.evbox.service.timewheel;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SessionCountTimeWheelTest {

    @Test
    public void shouldReturnTotalStartedSessionCountInLastMinute() {

        // Given
        SessionCountTimeWheel timeWheel = SessionCountTimeWheel.instance();

        // When
        timeWheel.increaseSessionStartedCount();

        // Then
        assertEquals(1, timeWheel.getTotalSessionsStartedInLast60Seconds());
    }

    @Test
    public void shouldReturnTotalFinishedSessionCountInLastMinute() {

        // Given
        SessionCountTimeWheel timeWheel = SessionCountTimeWheel.instance();

        // When
        timeWheel.increaseSessionFinishedCount();

        // Then
        assertEquals(1, timeWheel.getTotalFinishedSessionsInLast60Seconds());
    }

    @Test
    public void shouldReturnTotalOngoingSessionCountInLastMinute() {

        // Given
        SessionCountTimeWheel timeWheel = SessionCountTimeWheel.instance();

        // When
        timeWheel.increaseSessionStartedCount();
        timeWheel.increaseSessionStartedCount();
        timeWheel.increaseSessionFinishedCount();

        // Then
        assertEquals(1, timeWheel.getTotalOngoingSessionsInLast60Seconds());
    }

    @Test
    public void timeWheelShouldMoveFullCycleEvery60Seconds() throws InterruptedException {

        // Given
        SessionCountTimeWheel timeWheel = SessionCountTimeWheel.instance();

        // When
        timeWheel.increaseSessionStartedCount();
        timeWheel.increaseSessionStartedCount();
        timeWheel.increaseSessionFinishedCount();

        //TODO: Making SessionCountTimeWheel class configuration will help running this test faster.
        Thread.sleep(61000);

        // Then
        assertEquals(0, timeWheel.getTotalSessionsStartedInLast60Seconds());
        assertEquals(0, timeWheel.getTotalFinishedSessionsInLast60Seconds());
        assertEquals(1, timeWheel.getTotalOngoingSessionsInLast60Seconds());
    }

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, InstantiationException {

        Constructor<?> privateConstructor = SessionCountTimeWheel.class.getDeclaredConstructors()[0];
        privateConstructor.setAccessible(true);

        Field instance = SessionCountTimeWheel.class.getDeclaredField("instance");
        instance.setAccessible(true);

        instance.set(null, privateConstructor.newInstance());
    }

}