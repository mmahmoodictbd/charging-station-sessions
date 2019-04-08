package com.unloadbrain.assignement.evbox.config;

import com.unloadbrain.assignement.evbox.service.LoggedInUserService;
import com.unloadbrain.assignement.evbox.service.timewheel.SessionCountTimeWheel;
import com.unloadbrain.assignement.evbox.util.DateUtil;
import com.unloadbrain.assignement.evbox.util.UuidUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class AppConfig {

    /**
     * For assignment purpose, getLoggedInUserStationId() returns random uuid.
     * In production, we will have security configured, there will be one-to-one relation between
     * logged in user and stationId
     */
    @Bean
    public LoggedInUserService loggedInUserService() {
        return () -> UUID.randomUUID().toString();
    }

    @Bean
    public DateUtil dateUtil() {
        return new DateUtil();
    }

    @Bean
    public UuidUtil uuidConfig() {
        return new UuidUtil();
    }

    @Bean
    public SessionCountTimeWheel sessionCountTimeWheel() {
        return SessionCountTimeWheel.instance();
    }

}
