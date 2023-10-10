package de.iav.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.model.TimeSlotResponseDTO;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "timeslots")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
class NewTimeSlotControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final static String BASE_URL = "/api/toast-master-manager/timeslots";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TimeSlotWithoutIdDTO timeSlot1 = new TimeSlotWithoutIdDTO("Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00");
    private final TimeSlotWithoutIdDTO timeSlot2 = new TimeSlotWithoutIdDTO("Rede2", "Rede 2 226 vorbereitet", "4:00", "5:30", "6:00");

    @BeforeEach
    void insertTestTimeSlots() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot1))
        );
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(timeSlot2))
        );
    }

    @Test
    void getAllTimeSlots_shouldReturnAllEntries_whenTwoEntriesAreSavedAndNoSearchParamsAreDefined() throws Exception {

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("[0].id").isNotEmpty())
                .andExpect(jsonPath("[0].title").value(timeSlot1.getTitle()))
                .andExpect(jsonPath("[1].id").isNotEmpty())
                .andExpect(jsonPath("[1].title").value(timeSlot2.getTitle()))
                .andExpect(status().isOk());
    }

    @Test
    void getTimeSlotsByTitle_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {

        mockMvc.perform(get(BASE_URL + "?title=" + timeSlot1.getTitle()))
                .andExpect(jsonPath("[0].title").value(timeSlot1.getTitle()))
                .andExpect(jsonPath("$", hasSize(1)))
        ;
    }

    @Test
    void getNoTimeSlotsBySearchAttribut_shouldReturnReturn404_whenOneFittingEntryNotExists() throws Exception {

        mockMvc.perform(get(BASE_URL + "?title= gibtEsNicht"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(BASE_URL + "?description= gibtEsNicht"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(BASE_URL + "?green= gibtEsNicht"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(BASE_URL + "?amber= gibtEsNicht"))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(BASE_URL + "?red= gibtEsNicht"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getAllTimeSlotsByDescription_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "?description=" + timeSlot1.getDescription()))
                .andExpect(jsonPath("[0].description").value(timeSlot1.getDescription()))
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    void getAllTimeSlotsByGreen_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "?green=" + timeSlot1.getGreen()))
                .andExpect(jsonPath("[0].green").value(timeSlot1.getGreen()))
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    void getAllTimeSlotsByAmber_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "?amber=" + timeSlot1.getAmber()))
                .andExpect(jsonPath("[0].amber").value(timeSlot1.getAmber()))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getAllTimeSlotsByRed_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "?red=" + timeSlot1.getRed()))
                .andExpect(jsonPath("[0].red").value(timeSlot1.getRed()))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void searchTimeslot_ByTitleAndDescription() throws Exception {
        mockMvc.perform(get(BASE_URL + "/search?title=" + timeSlot1.getTitle() + "&description=" + timeSlot1.getDescription()))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(timeSlot1.getTitle()))
                .andExpect(jsonPath("description").value(timeSlot1.getDescription()));
    }

    @Test
    void searchTimeslot_ByTitleAndRed() throws Exception {
        mockMvc.perform(get(BASE_URL + "/search2?title=" + timeSlot1.getTitle() + "&red=" + timeSlot1.getRed()))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(timeSlot1.getTitle()))
                .andExpect(jsonPath("red").value(timeSlot1.getRed()));
    }

    @Test
    void getTimeSlotById_shouldReturnRequestedTimeSlot_whenMatchingIdIsProvided() throws Exception {
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });
        TimeSlotResponseDTO firstTimeSlot = timeSlotsList.get(0);

        mockMvc.perform(get(BASE_URL + "/" + firstTimeSlot.getId()))
                .andExpect(jsonPath("id").value(firstTimeSlot.getId()));
    }

    @Test
    void addTimeSlot_shouldCreateNewTimeSlotWithId_whenValidDataIsProvided() throws Exception {
        TimeSlotWithoutIdDTO timeSlot3 = new TimeSlotWithoutIdDTO("Rede x", "Rede x vorbereitet", "1:00", "1:30", "2:00");

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeSlot3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(timeSlot3.getTitle())
                );
    }

    @Test
    void addTimeSlot_shouldReturn400_whenInvalidDataIsProvided() throws Exception {
        TimeSlotWithoutIdDTO timeSlotTitle = new TimeSlotWithoutIdDTO(null, "Rede x vorbereitet", "1:00", "1:30", "2:00");
        TimeSlotWithoutIdDTO timeSlotDescription = new TimeSlotWithoutIdDTO("Rede x", null, "1:00", "1:30", "2:00");

        TimeSlotWithoutIdDTO timeSlotRed = new TimeSlotWithoutIdDTO("Rede x", "Rede x vorbereitet", null, "1:30", null);
        


        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeSlotTitle)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeSlotDescription)))
                .andExpect(status().isBadRequest());


        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeSlotRed)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateTimeSlot_shouldUpdateTimeSlotAccordingly_whenUserHasAdminRoleAndTimeSlotExists() throws Exception {
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });
        TimeSlotResponseDTO originalTimeSlot = timeSlotsList.get(0);
        TimeSlotResponseDTO updatedFirstTimeSlot = new TimeSlotResponseDTO(originalTimeSlot.getId(),"word of the Day", originalTimeSlot.getDescription(), originalTimeSlot.getGreen(), originalTimeSlot.getAmber(), originalTimeSlot.getRed());

        mockMvc.perform(put(BASE_URL + "/" + originalTimeSlot.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedFirstTimeSlot)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(originalTimeSlot.getId()))
                .andExpect(jsonPath("title").value(updatedFirstTimeSlot.getTitle()))
                .andExpect(jsonPath("description").value(updatedFirstTimeSlot.getDescription())
                );
    }

    @Test
    void updateTimeSlot_shouldReturn400_whenValuesInBodyArentValidAndIdIsInvalid() throws Exception {
        String THIS_ID_DOES_NOT_EXIST = "THIS_ID_NUMBER_DOES_NOT_EXIST";
        TimeSlotWithoutIdDTO timeSlotThatDoesntExist = new TimeSlotWithoutIdDTO("?", "?", "?", "?", "?");

        mockMvc.perform(put(BASE_URL + "/" + THIS_ID_DOES_NOT_EXIST)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeSlotThatDoesntExist)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTimeSlot_shouldReturn400_whenValuesInBodyArentValidAndIdIsValid() throws Exception {
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });
        TimeSlotResponseDTO originalTimeSlot = timeSlotsList.get(0);

        TimeSlotWithoutIdDTO invalidRequestBody = new TimeSlotWithoutIdDTO("?", "?", "?", "?", "?");

        mockMvc.perform(put(BASE_URL + "/" + originalTimeSlot.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTimeSlot_shouldReturn404_whenTimeSlotDoesntExist() throws Exception {
        String THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST = "THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST";
        TimeSlotResponseDTO timeSlotThatDoesntExist = new TimeSlotResponseDTO(THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST,"Nobody", "Nobody", "Nobody@Nobody.de", "Nobody", "noTime");

        mockMvc.perform(put(BASE_URL + "/123" )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(timeSlotThatDoesntExist)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteTimeSlot_shouldDeleteTimeSlotById_whenTimeSlotExistsAndUserHasAdminRole() throws Exception {
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });
        TimeSlotResponseDTO timeSlotToDelete = timeSlotsList.get(0);

        mockMvc.perform(delete(BASE_URL + "/" + timeSlotToDelete.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/" + timeSlotToDelete.getId()))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteTimeSlot_shouldReturn404_whenTimeSlotDoesntExistAndUserHasAdminRole() throws Exception {
        String THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST = "THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST";

        mockMvc.perform(delete(BASE_URL + "/" + THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTimeSlot_shouldReturn403_whenUserHasNoAdminRole() throws Exception {
        String timeSlotsListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<TimeSlotResponseDTO> timeSlotsList = objectMapper.readValue(timeSlotsListAsString, new TypeReference<>() {
        });
        TimeSlotResponseDTO originalTimeSlot = timeSlotsList.get(0);

        mockMvc.perform(delete(BASE_URL + "/" + originalTimeSlot.getId()))
                .andExpect(status().isForbidden());
    }

}
