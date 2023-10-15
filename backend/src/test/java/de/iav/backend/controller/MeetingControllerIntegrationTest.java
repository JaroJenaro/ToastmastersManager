package de.iav.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.model.*;
import de.iav.backend.security.NewAppUser;
import org.hamcrest.core.IsNull;
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
    private final static String BASE_URL_TS = "/api/toast-master-manager/timeslots";
    private final static String BASE_URL_USR = "/api/toast-master-manager/users";
    private final static String BASE_URL_MEET = "/api/toast-master-manager/meetings";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final NewAppUser user1 = new NewAppUser("Putin", "Wladimir", "Putin", "1234", "wladimir.putin@udssr.ru");
    private final NewAppUser user2 = new NewAppUser("Trump", "Donald", "Trump", "1234", "donald.trump@usa.us");
    private final NewAppUser user3 = new NewAppUser("Kaczynski", "Jaroslaw", "Kaczynski", "1234", "jaroslaw.Kaczynski@idiot.to");
    private final TimeSlotWithoutIdDTO timeSlot1 = new TimeSlotWithoutIdDTO("Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00");
    private final TimeSlotWithoutIdDTO timeSlot2 = new TimeSlotWithoutIdDTO("Rede2", "Rede 2 226 vorbereitet", "4:00", "5:30", "6:00");
    private final TimeSlotWithoutIdDTO timeSlot3 = new TimeSlotWithoutIdDTO("Rede3", "Rede3 vorbereitet", "4:00", "5:30", "7:00");

    private SpeechContributionDTO SpeechContributionIn1;
    private SpeechContributionDTO SpeechContributionIn2;
    private SpeechContributionDTO SpeechContributionIn3;
    private SpeechContributionDTO SpeechContributionIn4;
    private SpeechContributionDTO SpeechContributionIn5;
    private SpeechContributionDTO SpeechContributionIn6;

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
        mockMvc.perform(post(BASE_URL_TS)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot2))
        );

        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL_TS))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsDtoList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });

        SpeechContributionIn1 = new SpeechContributionDTO(null,timeSlotsDtoList.get(0),userResponseDtoList.get(0),"" );
        SpeechContributionIn2 = new SpeechContributionDTO(null,timeSlotsDtoList.get(1),userResponseDtoList.get(1),"" );
        SpeechContributionIn3 = new SpeechContributionDTO(null,timeSlotsDtoList.get(0),userResponseDtoList.get(1),"" );
        SpeechContributionIn4 = new SpeechContributionDTO(null,timeSlotsDtoList.get(1),userResponseDtoList.get(0),"" );

        List<SpeechContributionDTO> speechContributionsDtoList1 = new ArrayList<>();
        speechContributionsDtoList1.add(SpeechContributionIn1);
        speechContributionsDtoList1.add(SpeechContributionIn2);

        meetingRequestDto1 = new MeetingRequestDTO("2023.09.13 19:00 Uhr", "Braunschweig",speechContributionsDtoList1);
        mockMvc.perform(post(BASE_URL_MEET)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(meetingRequestDto1))
        );

        List<SpeechContributionDTO> speechContributionsDtoList2 = new ArrayList<>();
        speechContributionsDtoList1.add(SpeechContributionIn3);
        speechContributionsDtoList1.add(SpeechContributionIn4);

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

    @Test
    void createMeeting_shouldCreateNewMeetingWithId_whenValidDataIsProvided() throws Exception {
        mockMvc.perform(post(BASE_URL_USR)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user3))
        );
        String usersListAsString = mockMvc.perform(get(BASE_URL_USR))
                .andReturn().getResponse().getContentAsString();
        List<UserResponseDTO> userResponseDtoList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });

        mockMvc.perform(post(BASE_URL_TS)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot3))
        );
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL_TS))
                .andReturn().getResponse().getContentAsString();
        List<TimeSlotResponseDTO> timeSlotsDtoList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });

        SpeechContributionIn5 = new SpeechContributionDTO(null,timeSlotsDtoList.get(2),userResponseDtoList.get(2),"" );

        List<SpeechContributionDTO> speechContributionsDtoListNew = new ArrayList<>();
        speechContributionsDtoListNew.add(SpeechContributionIn1);
        speechContributionsDtoListNew.add(SpeechContributionIn2);
        speechContributionsDtoListNew.add(SpeechContributionIn3);
        speechContributionsDtoListNew.add(SpeechContributionIn4);
        speechContributionsDtoListNew.add(SpeechContributionIn5);

        MeetingRequestDTO meetingRequestDto3 = new MeetingRequestDTO("2023.10.13 19:00 Uhr", "Berlin",speechContributionsDtoListNew);

        mockMvc.perform(post(BASE_URL_MEET)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(meetingRequestDto3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("dateTime").value(meetingRequestDto3.getDateTime()))
                .andExpect(jsonPath("location").value(meetingRequestDto3.getLocation()))
                .andExpect(jsonPath("speechContributionList[4].user.firstName").value(meetingRequestDto3.getSpeechContributionList().get(4).getUser().getFirstName())
                );
    }

    @Test
    void createMeeting_shouldCreateNewMeetingWithId_whenValidDataIsProvidedAndUserIsNull() throws Exception {
        mockMvc.perform(post(BASE_URL_TS)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot3))
        );
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL_TS))
                .andReturn().getResponse().getContentAsString();
        List<TimeSlotResponseDTO> timeSlotsDtoList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });

        SpeechContributionIn5 = new SpeechContributionDTO(null, timeSlotsDtoList.get(2),null,"" );
        SpeechContributionIn6 = new SpeechContributionDTO(null,timeSlotsDtoList.get(2),null,"" );

        List<SpeechContributionDTO> speechContributionsDtoListNew = new ArrayList<>();
        speechContributionsDtoListNew.add(SpeechContributionIn1);
        speechContributionsDtoListNew.add(SpeechContributionIn2);
        speechContributionsDtoListNew.add(SpeechContributionIn3);
        speechContributionsDtoListNew.add(SpeechContributionIn4);
        speechContributionsDtoListNew.add(SpeechContributionIn5);
        speechContributionsDtoListNew.add(SpeechContributionIn6);

        MeetingRequestDTO meetingRequestDto3 = new MeetingRequestDTO("2023.10.13 19:00 Uhr", "Berlin",speechContributionsDtoListNew);

        mockMvc.perform(post(BASE_URL_MEET)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(meetingRequestDto3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("dateTime").value(meetingRequestDto3.getDateTime()))
                .andExpect(jsonPath("location").value(meetingRequestDto3.getLocation()))
                .andExpect(jsonPath("speechContributionList[3].user.firstName").value(meetingRequestDto3.getSpeechContributionList().get(3).getUser().getFirstName()))
                .andExpect(jsonPath("speechContributionList[5].user").value(meetingRequestDto3.getSpeechContributionList().get(5).getUser()))
                .andExpect(jsonPath("speechContributionList[5].user").value(IsNull.nullValue())
                );
    }
}