package com.unloadbrain.assignement.evbox.exception;

import com.unloadbrain.assignement.evbox.Application;
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

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class, BaseExceptionHandlerIT.TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class BaseExceptionHandlerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChargingSessionService chargingSessionServiceMock;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void runtimeExceptionShouldBeCaughtByGlobalExceptionHandler() throws Exception {

        doThrow(new RuntimeException("Something bad")).when(chargingSessionServiceMock).createSession();

        mockMvc.perform(post("/chargingSession"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected internal server error occurred"))
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