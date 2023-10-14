package de.iav.backend.service;

import de.iav.backend.exception.SpeechContributionNotFoundException;
import de.iav.backend.model.*;
import de.iav.backend.repository.SpeechContributionRepository;
import de.iav.backend.repository.TimeSlotRepository;
import de.iav.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpeechContributionServiceTest {


    private final TimeSlotRepository timeSlotRepository = mock(TimeSlotRepository.class);
    private final TimeSlotService timeSlotService = new TimeSlotService(timeSlotRepository);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);
    private final SpeechContributionRepository speechContributionRepository = mock(SpeechContributionRepository.class);
    private final SpeechContributionService speechContributionService = new SpeechContributionService(speechContributionRepository,  timeSlotRepository, userRepository);



    @Test
    void getAllSpeechContributions_whenNoSpeechContributionsAreAvailable_thenReturnEmptyList() {
        // GIVEN
        List<SpeechContribution> expected = List.of();
        // WHEN
        when(speechContributionRepository.findAll()).thenReturn(expected);
        List<SpeechContributionDTO> actual = speechContributionService.getAllSpeechContributions();
        // THEN
        List<SpeechContributionDTO> expectedResponse = new ArrayList<>();

        assertEquals(expectedResponse, actual);
        verify(speechContributionRepository).findAll();
    }

    @Test
    void getAllSpeechContributions_whenSpeechContributionsAvailable_thenReturnListOfSpeechContributions() {
        // GIVEN
        List<SpeechContribution> expected = List.of(
                new SpeechContribution("123",new TimeSlot( "12345","Rede1", "Rede 1 154 vorbereitet", "1:00", "1:30", "2:00"),
                        new User("231", "Wladimir", "Putin", "wladimir.putin@udssr.ru", "1234","ADMIN"), ""
                ),
                new SpeechContribution("124",new TimeSlot( "12346","Rede2", "Rede 2 226 vorbereitet", "4:00", "5:30", "6:00"),
                        new User("232",  "Donald", "Trump",  "donald.trump@usa.us", "1234","USER"), "")
                ,
                new SpeechContribution("125",new TimeSlot( "12347","Rede3", "Rede 3 irgendwie", "4:00", "5:30", "6:00"),
                        new User("233",  "Alice", "Wagenknecht",  "alice.wagenknecht@ss.de", "1234","USER"), "")

        );




        // WHEN
        when(speechContributionRepository.findAll()).thenReturn(expected);

        List<SpeechContributionDTO> actual = speechContributionService.getAllSpeechContributions();
        // THEN
        List<SpeechContributionDTO> expectedResponse;
        expectedResponse = expected.stream()
                .map(speechContribution -> SpeechContributionDTO.builder()
                        .id(speechContribution.getId())
                        .timeSlot(timeSlotService.getTimeSlotResponseDTO(speechContribution.getTimeSlot()))
                        .user(userService.getUserResponseDTO(speechContribution.getUser()))
                        .stoppedTime(speechContribution.getStoppedTime())

                        .build())
                .toList();

        assertEquals(expectedResponse, actual);
        verify(speechContributionRepository).findAll();
    }

    @Test
    void getSpeechContributionById_whenSpeechContributionWithGivenIdExist_thenReturnSpeechContributionById() {
        // GIVEN
        Optional<SpeechContribution> expected = Optional.of( new SpeechContribution("125",new TimeSlot( "12347","Rede3", "Rede 3 irgendwie", "4:00", "5:30", "6:00"),
                new User("233",  "Alice", "Wagenknecht",  "alice.wagenknecht@ss.de", "1234","USER"), ""));
        // WHEN
        when(speechContributionRepository.findById("1")).thenReturn(expected);
        SpeechContributionDTO actual = speechContributionService.getSpeechContributionById("1");
        // THEN
        assertEquals(expected.get().getId(), actual.getId());
        verify(speechContributionRepository).findById("1");
    }

    @Test
    void getSpeechContributionById_whenSpeechContributionWithGivenIdNotExist_thenThrowSpeechContributionNotFoundException() {
        // GIVEN
        String id = "1";
        // WHEN
        when(speechContributionRepository.findById(id)).thenThrow(new SpeechContributionNotFoundException(id));
        // THEN
        assertThrows(SpeechContributionNotFoundException.class, () -> speechContributionService.getSpeechContributionById(id));
        verify(speechContributionRepository).findById(id);
    }


}