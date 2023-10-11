package de.iav.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "timeslots")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
class SpeechContributionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final static String BASE_URL = "/api/toast-master-manager/speech-contributions";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SpeechContributionIn SpeechContributionIn1 = new SpeechContributionIn(new TimeSlotResponseDTO( "12345","Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00"),
            new UserResponseDTO("124", "Wladimir", "Putin", "wladimir.putin@udssr.ru", "ADMIN"), "");

    private final SpeechContributionIn SpeechContributionIn2 = new SpeechContributionIn(new TimeSlotResponseDTO( "12346","Rede2", "Rede 2 226 vorbereitet", "4:00", "5:30", "6:00"),
            new UserResponseDTO("125",  "Donald", "Trump",  "donald.trump@usa.us","USER"), "");

    @BeforeEach
    void insertTestSpeechContributions() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(SpeechContributionIn1))
        );
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(SpeechContributionIn2))
        );
    }

    @Test
    void getAllSpeechContributions() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("[0].id").isNotEmpty())
                .andExpect(jsonPath("[0].stoppedTime").value(SpeechContributionIn1.getStoppedTime()))
                .andExpect(jsonPath("[1].id").isNotEmpty())
                .andExpect(jsonPath("[1].stoppedTime").value(SpeechContributionIn2.getStoppedTime()))
                .andExpect(status().isOk());
    }

    @Test
    void getSpeechContributionById_shouldReturnRequestedUser_whenMatchingIdIsProvided() throws Exception {
        String usersListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<SpeechContributionDTO> speechContributionList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        SpeechContributionDTO firsSpeechContributionDTO = speechContributionList.get(0);

        mockMvc.perform(get(BASE_URL + "/" + firsSpeechContributionDTO.getId()))
                .andExpect(jsonPath("id").value(firsSpeechContributionDTO.getId()));
    }

    @Test
    void createSpeechContribution_shouldCreateNewSpeechContributionWithId_whenValidDataIsProvided() throws Exception {
        SpeechContributionIn speechContribution3 = new SpeechContributionIn(SpeechContributionIn1.getTimeSlotDto(), SpeechContributionIn2.getUserDto(), "");

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(speechContribution3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("stoppedTime").value(speechContribution3.getStoppedTime())
                );
    }
}