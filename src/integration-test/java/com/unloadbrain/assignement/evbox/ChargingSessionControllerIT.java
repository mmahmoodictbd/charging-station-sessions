package com.unloadbrain.assignement.evbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unloadbrain.assignement.evbox.dto.response.IdentityResponse;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
@ActiveProfiles("it")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ChargingSessionControllerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    private static String sessionId;

    @Before
    public void setup() throws Exception {

        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        if (sessionId == null) {
            MvcResult result = mockMvc.perform(post("/chargingSession"))
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                    .andExpect(status().isCreated())
                    .andDo(print()).andReturn();
            sessionId = new ObjectMapper()
                    .readValue(result.getResponse().getContentAsString(), IdentityResponse.class).getId();
        }
    }

    @Test
    public void createSessionShouldReflectInSummery() throws Exception {

        // When
        // Session created in setup()

        // Then
        mockMvc.perform(get("/chargingSessions/started"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startedCount").value("1"))
                .andDo(print());

        mockMvc.perform(get("/chargingSessions"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startedCount").value("1"))
                .andExpect(jsonPath("$.stoppedCount").value("0"))
                .andExpect(jsonPath("$.totalCount").value("1"))
                .andDo(print());

    }

    @Test
    public void stopSessionShouldReflectInSummery() throws Exception {

        // Given
        // Session created in setup()

        // When
        mockMvc.perform(put("/chargingSession/" + sessionId))
                .andExpect(status().isNoContent())
                .andDo(print());

        // Then
        mockMvc.perform(get("/chargingSessions"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startedCount").value("1"))
                .andExpect(jsonPath("$.stoppedCount").value("1"))
                .andExpect(jsonPath("$.totalCount").value("0"))
                .andDo(print());

        mockMvc.perform(get("/chargingSessions/stopped"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stoppedCount").value("1"))
                .andDo(print());
    }

}