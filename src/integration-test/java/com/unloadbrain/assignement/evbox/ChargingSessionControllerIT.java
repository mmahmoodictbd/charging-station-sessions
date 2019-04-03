package com.unloadbrain.assignement.evbox;

import com.unloadbrain.assignement.evbox.controller.ChargingSessionController;
import com.unloadbrain.assignement.evbox.dto.response.ChargingSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStartedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.ChargingStoppedSessionsSummeryResponse;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import com.unloadbrain.assignement.evbox.service.ChargingSessionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class ChargingSessionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private ChargingSessionService chargingSessionServiceMock;

    @Before
    public void setup() {
        this.chargingSessionServiceMock = mock(ChargingSessionService.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new ChargingSessionController(chargingSessionServiceMock)).build();
    }

    @Test
    public void shouldCreateChargingSession() throws Exception {

        when(chargingSessionServiceMock.createSession(any())).thenReturn(new IdentityResponse("uuid"));

        mockMvc.perform(post("/chargingSession")
                .content("{ \"stationId\": \"stations1\" }")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(not(empty()))))
                .andDo(print());
    }

    @Test
    public void shouldReturnChargingSessionSummery() throws Exception {

        when(chargingSessionServiceMock.getSessionSummery()).thenReturn(
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

        when(chargingSessionServiceMock.getStartedSessionSummery()).thenReturn(
                ChargingStartedSessionsSummeryResponse.builder().startedCount(1L).build());

        mockMvc.perform(get("/chargingSessions/started"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startedCount").value("1"))
                .andDo(print());
    }

    @Test
    public void shouldReturnChargingStoppedSessionSummery() throws Exception {

        when(chargingSessionServiceMock.getStoppedSessionSummery()).thenReturn(
                ChargingStoppedSessionsSummeryResponse.builder().stoppedCount(1L).build());

        mockMvc.perform(get("/chargingSessions/stopped"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stoppedCount").value("1"))
                .andDo(print());
    }

    @Test
    public void shouldDeleteStoppedSessionsSummery() throws Exception {

        doNothing().when(chargingSessionServiceMock).deleteStoppedChargingSessions();

        mockMvc.perform(delete("/chargingSessions"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

}