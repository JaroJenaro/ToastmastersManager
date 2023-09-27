package de.iav.backend.service;

import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.model.TimeSlot;
import de.iav.backend.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class TimeSlotServiceTest {


    private final TimeSlotRepository timeSlotRepository = mock(TimeSlotRepository.class);
    private final TimeSlotService timeSlotService = new TimeSlotService(timeSlotRepository);

    @Test
    void getAllTimeSlots_whenNoTimeSlotsAvailable_thenReturnEmptyList() {
        // GIVEN
        List<TimeSlot> expected = List.of();
        // WHEN
        when(timeSlotRepository.findAll()).thenReturn(expected);
        List<TimeSlot> actual = timeSlotService.getAllTimeSlots();
        // THEN
        assertEquals(expected, actual);
        verify(timeSlotRepository).findAll();
    }

    @Test
    void getAllTimeSlots_whenTimeSlotsAvailable_thenReturnListOfTimeSlots() {
        // GIVEN
        List<TimeSlot> expected = List.of(
                new TimeSlot("1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00"),
                new TimeSlot("2 Speaker", "Zweite vorbereitete Rede", "5:00", "6:00", "7:00"),
                new TimeSlot("3 Speaker", "Drutte vorbereitete Rede", "5:00", "6:00", "7:00")
        );
        // WHEN
        when(timeSlotRepository.findAll()).thenReturn(expected);
        List<TimeSlot> actual = timeSlotService.getAllTimeSlots();
        // THEN
        assertEquals(expected, actual);
        verify(timeSlotRepository).findAll();
    }

    @Test
    void getTimeSlotById_whenTimeSlotWithGivenIdExist_thenReturnTimeSlotById() {
        // GIVEN
        Optional<TimeSlot> expected = Optional.of(new TimeSlot("1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00"));
        // WHEN
        when(timeSlotRepository.findById("1")).thenReturn(expected);
        Optional<TimeSlot> actual = timeSlotService.getTimeSlotById("1");
        // THEN
        assertEquals(expected, actual);
        verify(timeSlotRepository).findById("1");
    }

    @Test
    void getTimeSlotById_whenTimeSlotWithGivenIdNotExist_thenThrowTimeSlotNotFoundException() {
        // GIVEN
        String id = "1";
        // WHEN
        when(timeSlotRepository.findById(id)).thenThrow(new TimeSlotNotFoundException(id));
        // THEN
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotById(id));
        verify(timeSlotRepository).findById(id);
    }

    @Test
    void addTimeSlot_whenCalled_thenSaveAndReturnTimeSlot() {
        // GIVEN
        TimeSlot expected = new TimeSlot("1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00");
        // WHEN
        when(timeSlotRepository.save(expected)).thenReturn(expected);
        TimeSlot actual = timeSlotService.saveTimeSlot (expected);
        // THEN
        assertEquals(expected, actual);
        verify(timeSlotRepository).save(expected);
    }

    @Test
    void deleteTimeSlot_whenTimeSlotWithGivenIdExist_thenDeleteTimeSlotAndNoReturn() {
        // GIVEN
        String id = "1";
        TimeSlot timeSlotToDelete = new TimeSlot("1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00");
        timeSlotRepository.save(timeSlotToDelete);
        // WHEN
        when(timeSlotRepository.findById(id)).thenReturn(Optional.of(timeSlotToDelete));
        doNothing().when(timeSlotRepository).deleteById(id);
        timeSlotService.deleteTimeSlot(id);
        // THEN
        verify(timeSlotRepository).deleteById(id);
    }

    @Test
    void deleteTimeSlot_whenTimeSlotWithGivenIdNotExist_thenThrowTimeSlotNotFoundException() {
        // GIVEN
        String id = "1";
        // WHEN
        doThrow(TimeSlotNotFoundException.class).when(timeSlotRepository).deleteById(id);
        // THEN
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.deleteTimeSlot(id));
        verify(timeSlotRepository).deleteById(id);
    }


    @Test
    void updateTimeSlot_whenTimeSlotExist_thenUpdateAndReturnTimeSlot() {
        // GIVEN
        //String id = "1";
        TimeSlot expected = new TimeSlot("1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00");
        // WHEN
        when(timeSlotRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(timeSlotRepository.save(expected)).thenReturn(expected);
        TimeSlot actual = timeSlotService.saveTimeSlot( expected);
        // THEN
        assertEquals(expected, actual);
        verify(timeSlotRepository).save(expected);
    }

    /*
    @Test
    void setTimeSlotRepository() {
    }*/


}