package de.iav.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.model.*;
import de.iav.backend.security.NewAppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private final static String BASE_URL_TS = "/api/toast-master-manager/timeslots";
    private final static String BASE_URL_USR = "/api/toast-master-manager/users";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final NewAppUser user1 = new NewAppUser("Putin", "Wladimir", "Putin", "1234", "wladimir.putin@udssr.ru");
    private final NewAppUser user2 = new NewAppUser("Trump", "Donald", "Trump", "1234", "donald.trump@usa.us");
    private final TimeSlotWithoutIdDTO timeSlot1 = new TimeSlotWithoutIdDTO("Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00");
    private final TimeSlotWithoutIdDTO timeSlot2 = new TimeSlotWithoutIdDTO("Rede2", "Rede 2 226 vorbereitet", "4:00", "5:30", "6:00");

    private SpeechContributionIn SpeechContributionIn1;

    private SpeechContributionIn SpeechContributionIn2;

    @BeforeEach
    void insertTestSpeechContributions() throws Exception {
        mockMvc.perform(post(BASE_URL_USR)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user1))
        );
        mockMvc.perform(post(BASE_URL_USR)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user2))
        );

        String usersListAsString = mockMvc.perform(get(BASE_URL_USR))
                .andReturn().getResponse().getContentAsString();

        List<UserResponseDTO> userResponseDtoList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        mockMvc.perform(post(BASE_URL_TS)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot1))
        );
        mockMvc.perform(post(BASE_URL_TS)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot2))
        );

        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL_TS))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsDtoList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });

        SpeechContributionIn1 = new SpeechContributionIn(timeSlotsDtoList.get(0),userResponseDtoList.get(0),"" );
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(SpeechContributionIn1))
        );

        SpeechContributionIn2 = new SpeechContributionIn(timeSlotsDtoList.get(1),userResponseDtoList.get(1),"" );
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
    void getSpeechContributionById2_shouldReturnRequestedUser_whenMatchingIdIsProvided() throws Exception {
        String speechContributionListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<SpeechContributionDTO> speechContributionList = objectMapper.readValue(speechContributionListAsString, new TypeReference<>() {
        });
        SpeechContributionDTO firsSpeechContributionDTO = speechContributionList.get(1);

        mockMvc.perform(get(BASE_URL + "/" + firsSpeechContributionDTO.getId()))
                .andExpect(jsonPath("id").value(firsSpeechContributionDTO.getId()));
    }

    @Test
    void createSpeechContribution_shouldCreateNewSpeechContributionWithId_whenValidDataIsProvided() throws Exception {
        SpeechContributionIn speechContribution3 = new SpeechContributionIn(SpeechContributionIn1.getTimeSlot(), SpeechContributionIn2.getUser(), "");

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(speechContribution3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("stoppedTime").value(speechContribution3.getStoppedTime())
                );
    }



    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateSpeechContribution_shouldUpdateSpeechContributionAccordingly_whenUserHasAdminRoleAndSpeechContributionExists() throws Exception {
        String speechContributionListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<SpeechContributionDTO> speechContributionList = objectMapper.readValue(speechContributionListAsString, new TypeReference<>() {
        });

        SpeechContributionDTO speechContributionDtoToUpdate = speechContributionList.get(1);

        SpeechContributionIn speechContributionNewData = new SpeechContributionIn(SpeechContributionIn2.getTimeSlot(), SpeechContributionIn1.getUser(), "14:45");


        mockMvc.perform(put(BASE_URL + "/" + speechContributionDtoToUpdate.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(speechContributionNewData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(speechContributionDtoToUpdate.getId()))
                .andExpect(jsonPath("stoppedTime").value(speechContributionNewData.getStoppedTime())
                );
    }

    @Test
    void updateSpeechContribution_shouldReturn404_whenSpeechContributionDoesntExist() throws Exception {

        SpeechContributionIn SpeechContributionIn1 = new SpeechContributionIn(new TimeSlotResponseDTO( "12345","Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00"),
                new UserResponseDTO("124", "Wladimir", "Putin", "wladimir.putin@udssr.ru", "ADMIN"), "");
        String THIS_ID_DOES_NOT_EXIST = "THIS_ID_NUMBER_DOES_NOT_EXIST";


        mockMvc.perform(put(BASE_URL + "/" + THIS_ID_DOES_NOT_EXIST)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(SpeechContributionIn1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTimeSlot_shouldReturn400_whenValuesInBodyArentValidAndIdIsValid() throws Exception {

        String speechContributionListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<SpeechContributionDTO> speechContributionList = objectMapper.readValue(speechContributionListAsString, new TypeReference<>() {
        });

        SpeechContributionDTO speechContributionDtoToUpdate = speechContributionList.get(0);
        SpeechContributionDTO speechContributionDtoSecond = speechContributionList.get(1);
        SpeechContributionIn invalidRequestBody = new SpeechContributionIn(speechContributionDtoSecond.getTimeSlot(),
                speechContributionDtoSecond.getUser(), "");

        mockMvc.perform(put(BASE_URL + "/" + speechContributionDtoToUpdate.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }


}