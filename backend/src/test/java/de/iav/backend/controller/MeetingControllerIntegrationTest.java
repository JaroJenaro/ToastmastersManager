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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "Arthur")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
class MeetingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final static String BASE_URL = "/api/toast-master-manager/speech-contributions";

    private final static String BASE_URL_TS = "/api/toast-master-manager/timeslots";
    private final static String BASE_URL_USR = "/api/toast-master-manager/users";

    private final static String BASE_URL_MEET = "/api/toast-master-manager/meetings";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final NewAppUser user1 = new NewAppUser("Putin", "Wladimir", "Putin", "1234", "wladimir.putin@udssr.ru");
    private final NewAppUser user2 = new NewAppUser("Trump", "Donald", "Trump", "1234", "donald.trump@usa.us");
    private final TimeSlotWithoutIdDTO timeSlot1 = new TimeSlotWithoutIdDTO("Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00");
    private final TimeSlotWithoutIdDTO timeSlot2 = new TimeSlotWithoutIdDTO("Rede2", "Rede 2 226 vorbereitet", "4:00", "5:30", "6:00");


    private SpeechContributionIn SpeechContributionIn1;
    private SpeechContributionIn SpeechContributionIn2;

    private SpeechContributionIn SpeechContributionIn3;
    private SpeechContributionIn SpeechContributionIn4;
    private MeetingRequestDTO meetingRequestDto1;
    private MeetingRequestDTO meetingRequestDto2;

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

        SpeechContributionIn3 = new SpeechContributionIn(timeSlotsDtoList.get(0),userResponseDtoList.get(1),"" );
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(SpeechContributionIn3))
        );

        SpeechContributionIn4 = new SpeechContributionIn(timeSlotsDtoList.get(1),userResponseDtoList.get(0),"" );
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(SpeechContributionIn4))
        );
        String speechContributionListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();
        List<SpeechContributionDTO> speechContributionsDtoList = objectMapper.readValue(speechContributionListAsString, new TypeReference<>() {
        });

        List<SpeechContributionDTO> speechContributionsDtoList1 = new ArrayList<>();
        speechContributionsDtoList1.add(speechContributionsDtoList.get(0));
        speechContributionsDtoList1.add(speechContributionsDtoList.get(1));

        meetingRequestDto1 = new MeetingRequestDTO("2023.09.13 19:00 Uhr", "Braunschweig",speechContributionsDtoList1);
        mockMvc.perform(post(BASE_URL_MEET)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(meetingRequestDto1))
        );

        List<SpeechContributionDTO> speechContributionsDtoList2 = new ArrayList<>();
        speechContributionsDtoList1.add(speechContributionsDtoList.get(2));
        speechContributionsDtoList1.add(speechContributionsDtoList.get(3));

        meetingRequestDto2 = new MeetingRequestDTO("2023.09.27 19:00 Uhr", "Hannover",speechContributionsDtoList2);
        mockMvc.perform(post(BASE_URL_MEET)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(meetingRequestDto2))
        );
    }

    @Test
    void getAllMeetings() throws Exception {
        mockMvc.perform(get(BASE_URL_MEET))
                .andExpect(jsonPath("[0].id").isNotEmpty())
                .andExpect(jsonPath("[0].location").isNotEmpty())
                .andExpect(jsonPath("[0].location").value(meetingRequestDto1.getLocation()))
                .andExpect(jsonPath("[0].dateTime").value(meetingRequestDto1.getDateTime()))
                .andExpect(jsonPath("[1].id").isNotEmpty())
                .andExpect(jsonPath("[1].location").isNotEmpty())
                .andExpect(jsonPath("[1].location").value(meetingRequestDto2.getLocation()))
                .andExpect(jsonPath("[1].dateTime").value(meetingRequestDto2.getDateTime()))
                .andExpect(status().isOk());
    }

    @Test
    void getMeetingById_shouldReturnMeeting_whenMatchingIdIsProvided() throws Exception {
        String meetingsListAsString = mockMvc.perform(get(BASE_URL_MEET))
                .andReturn().getResponse().getContentAsString();

        List<MeetingResponseDTO> meetingResponseDtoList = objectMapper.readValue(meetingsListAsString, new TypeReference<>() {
        });
        MeetingResponseDTO firsMeetingResponseDTO = meetingResponseDtoList.get(0);

        mockMvc.perform(get(BASE_URL_MEET + "/" + firsMeetingResponseDTO.getId()))
                .andExpect(jsonPath("id").value(firsMeetingResponseDTO.getId()));
    }

    @Test
    void getMeetingById2_shouldReturnMeeting_whenMatchingIdIsProvided() throws Exception {
        String meetingsListAsString = mockMvc.perform(get(BASE_URL_MEET))
                .andReturn().getResponse().getContentAsString();

        List<MeetingResponseDTO> meetingResponseDtoList = objectMapper.readValue(meetingsListAsString, new TypeReference<>() {
        });
        MeetingResponseDTO firsMeetingResponseDTO = meetingResponseDtoList.get(1);

        mockMvc.perform(get(BASE_URL_MEET + "/" + firsMeetingResponseDTO.getId()))
                .andExpect(jsonPath("id").value(firsMeetingResponseDTO.getId()));
    }
}