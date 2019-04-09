package com.unloadbrain.assignement.evbox;

import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import com.unloadbrain.assignement.evbox.exception.ChargingSessionNotFoundException;
import com.unloadbrain.assignement.evbox.service.ChargingSessionInMemoryService;
import com.unloadbrain.assignement.evbox.service.ChargingSessionService;
import com.unloadbrain.assignement.evbox.service.ChargingSessionStatisticsInMemoryService;
import com.unloadbrain.assignement.evbox.service.ChargingSessionStatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, ChargingSessionControllerContractIT.TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class ChargingSessionControllerContractIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChargingSessionService chargingSessionServiceMock;

    @Autowired
    private ChargingSessionStatisticsService chargingSessionStatisticsServiceMock;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldCreateChargingSession() throws Exception {

        when(chargingSessionServiceMock.createSession()).thenReturn(new IdentityResponse("uuid"));

        mockMvc.perform(post("/chargingSession"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("uuid"))
                .andDo(print());
    }

    @Test
    public void shouldStopChargingSession() throws Exception {

        doNothing().when(chargingSessionServiceMock).stopSession(anyString());

        mockMvc.perform(put("/chargingSession/uuid"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void shouldThrowExceptionWhenStopChargingSessionIdNotFound() throws Exception {

        doThrow(new ChargingSessionNotFoundException("Charging session uuid not found."))
                .when(chargingSessionServiceMock).stopSession("uuid");

        mockMvc.perform(put("/chargingSession/uuid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("SESSION_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("The session was not found"))
                .andDo(print());
    }

    @Test
    public void shouldReturnChargingSessionSummery() throws Exception {

        when(chargingSessionStatisticsServiceMock.getSessionSummery()).thenReturn(
                ChargingSessionsSummeryResponse.builder().startedCount(1L).stoppedCount(2L).totalCount(3L).build());

        mockMvc.perform(get("/chargingSessions"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startedCount").value("1"))
                .andExpect(jsonPath("$.stoppedCount").value("2"))
                .andExpect(jsonPath("$.totalCount").value("3"))
                .andDo(print());
    }

    @Test
    public void shouldReturnChargingStartedSessionSummery() throws Exception {

        when(chargingSessionStatisticsServiceMock.getStartedSessionSummery()).thenReturn(
                ChargingStartedSessionsSummeryResponse.builder().startedCount(1L).build());

        mockMvc.perform(get("/chargingSessions/started"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startedCount").value("1"))
                .andDo(print());
    }

    @Test
    public void shouldReturnChargingStoppedSessionSummery() throws Exception {

        when(chargingSessionStatisticsServiceMock.getStoppedSessionSummery()).thenReturn(
                ChargingStoppedSessionsSummeryResponse.builder().stoppedCount(1L).build());

        mockMvc.perform(get("/chargingSessions/stopped"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stoppedCount").value("1"))
                .andDo(print());
    }

    @Test
    public void shouldDeleteStoppedSessionsSummery() throws Exception {

        doNothing().when(chargingSessionServiceMock).deleteStoppedSessions();

        mockMvc.perform(delete("/chargingSessions"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Configuration
    static class TestConfig {

        @Primary
        @Bean
        public ChargingSessionService chargingSessionService() {
            return mock(ChargingSessionInMemoryService.class);
        }

        @Primary
        @Bean
        public ChargingSessionStatisticsService chargingSessionStatisticsService() {
            return mock(ChargingSessionStatisticsInMemoryService.class);
        }

    }

}