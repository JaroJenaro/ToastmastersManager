package de.iav.backend.service;

import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.model.TimeSlot;
import de.iav.backend.model.TimeSlotResponseDTO;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import de.iav.backend.repository.TimeSlotRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class TimeSlotServiceTest {


    private final TimeSlotRepository timeSlotRepository = mock(TimeSlotRepository.class);
    private final TimeSlotService timeSlotService = new TimeSlotService(timeSlotRepository);

    @Test
    void getAllTimeSlots_whenNoTimeSlotsNotAvailable_thenReturnEmptyList() {
        // GIVEN
        List<TimeSlot> expected = List.of();
        // WHEN
        when(timeSlotRepository.findAll()).thenReturn(expected);
        List<TimeSlotResponseDTO> actual = timeSlotService.getAllTimeSlots();
        // THEN
        List<TimeSlotResponseDTO> expectedResponse = new ArrayList<>();

        assertEquals(expectedResponse, actual);
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

        List<TimeSlotResponseDTO> actual = timeSlotService.getAllTimeSlots();
        // THEN
        List<TimeSlotResponseDTO> expectedResponse;
        expectedResponse = expected.stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();

        assertEquals(expectedResponse, actual);
        verify(timeSlotRepository).findAll();
    }

    @Test
    void getTimeSlotById_whenTimeSlotWithGivenIdExist_thenReturnTimeSlotById() {
        // GIVEN
        //TimeSlotDTO expected = new TimeSlotDTO("1","1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00"))
        Optional<TimeSlot> expected = Optional.of(new TimeSlot("1","1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00"));
        // WHEN
        when(timeSlotRepository.findById("1")).thenReturn(expected);
        TimeSlotResponseDTO actual = timeSlotService.getTimeSlotById("1");
        // THEN
        assertEquals(expected.get().getId(), actual.getId());
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
    void testAddTimeSlot() {
        TimeSlotWithoutIdDTO timeSlotWithoutIdDTO = new TimeSlotWithoutIdDTO("1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00");

        // Argument-Captor erstellen, um die Argumente zu erfassen
        ArgumentCaptor<TimeSlot> timeSlotCaptor = ArgumentCaptor.forClass(TimeSlot.class);

        // Mocking data for repository
        when(timeSlotRepository.save(timeSlotCaptor.capture())).thenReturn(new TimeSlot());

        TimeSlotResponseDTO result = timeSlotService.addTimeSlot(timeSlotWithoutIdDTO);

        assertNotNull(result);

        // Überprüfen Sie die erfassten Argumente
        TimeSlot capturedTimeSlot = timeSlotCaptor.getValue();
        assertEquals("1 Speaker", capturedTimeSlot.getTitle());
        assertEquals("Erste vorbereitete Rede", capturedTimeSlot.getDescription());
        assertEquals("4:00", capturedTimeSlot.getGreen());
        assertEquals("5:00", capturedTimeSlot.getAmber());
        assertEquals("6:00", capturedTimeSlot.getRed());
        //assertNotNull(capturedTimeSlot.getId());
    }






    @Test
    void addTimeSlot_whenCalled_thenSaveAndReturnTimeSlot() {
        // GIVEN
        TimeSlot expected = new TimeSlot("1","1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00");

        TimeSlot timeSlotWithoutId = TimeSlot.builder()
                .title(expected.getTitle())
                .description(expected.getDescription())
                .green(expected.getGreen())
                .amber(expected.getAmber())
                .red(expected.getRed())
                .build();
        TimeSlotWithoutIdDTO expectedWithoutIdDTO = TimeSlotWithoutIdDTO.builder()
                .title(expected.getTitle())
                .description(expected.getDescription())
                .green(expected.getGreen())
                .amber(expected.getAmber())
                .red(expected.getRed())
                .build();

        ArgumentCaptor<TimeSlot> timeSlotCaptor = ArgumentCaptor.forClass(TimeSlot.class);
        // WHEN

        when(timeSlotRepository.save(timeSlotCaptor.capture())).thenReturn(new TimeSlot());


        TimeSlotResponseDTO actualResponseDTO = timeSlotService.addTimeSlot(expectedWithoutIdDTO);

        // THEN
        //assertEquals(expected, actual);
        assertNotNull(actualResponseDTO);
        verify(timeSlotRepository).save(timeSlotWithoutId);
        //TimeSlot capturedTimeSlot = timeSlotCaptor.getValue();
        TimeSlot capturedTimeSlot = timeSlotCaptor.getValue();
        assertEquals("1 Speaker", capturedTimeSlot.getTitle());
        assertEquals("Erste vorbereitete Rede", capturedTimeSlot.getDescription());
        assertEquals("4:00", capturedTimeSlot.getGreen());
        assertEquals("5:00", capturedTimeSlot.getAmber());
        assertEquals("6:00", capturedTimeSlot.getRed());

    }

    @Test
    void testAddTimeSlot_fromGPT() {
        // Create a sample TimeSlotWithoutIdDTO
        TimeSlotWithoutIdDTO inputDTO = new TimeSlotWithoutIdDTO();
        inputDTO.setTitle("New Title");
        inputDTO.setDescription("New Description");
        inputDTO.setGreen("New Green");
        inputDTO.setAmber("New Amber");
        inputDTO.setRed("New Red");

        // Create a mock TimeSlot and specify its behavior
        TimeSlot mockTimeSlot = new TimeSlot();
        mockTimeSlot.setId("1");
        mockTimeSlot.setTitle("New Title");
        mockTimeSlot.setDescription("New Description");
        mockTimeSlot.setGreen("New Green");
        mockTimeSlot.setAmber("New Amber");
        mockTimeSlot.setRed("New Red");

        // Mock the repository's save method to return the mock TimeSlot
        when(timeSlotRepository.save(any(TimeSlot.class))).thenReturn(mockTimeSlot);

        // Call the service method
        TimeSlotResponseDTO result = timeSlotService.addTimeSlot(inputDTO);

        // Verify that the service method returns the expected DTO
        assertEquals("1", result.getId());
        assertEquals("New Title", result.getTitle());
        assertEquals("New Amber", result.getAmber());

        // Verify that the repository's save method was called with the correct parameters
        verify(timeSlotRepository, times(1)).save(argThat(argument -> {
            // Verify that the TimeSlot argument passed to save matches the inputDTO
            return "New Title".equals(argument.getTitle()) &&
                    "New Description".equals(argument.getDescription()) &&
                    "New Green".equals(argument.getGreen()) &&
                    "New Amber".equals(argument.getAmber()) &&
                    "New Red".equals(argument.getRed());
        }));
    }

    @Test
    void testUpdateTimeSlot() {
        // Create a sample TimeSlotWithoutIdDTO for update
        TimeSlotWithoutIdDTO inputDTO = new TimeSlotWithoutIdDTO();
        inputDTO.setTitle("Updated Title");
        inputDTO.setDescription("Updated Description");
        inputDTO.setGreen("Updated Green");
        inputDTO.setAmber("Updated Amber");
        inputDTO.setRed("Updated Red");

        // Create a mock TimeSlot for an existing record in the database
        TimeSlot existingTimeSlot = new TimeSlot();
        existingTimeSlot.setId("1");
        existingTimeSlot.setTitle("Original Title");
        existingTimeSlot.setDescription("Original Description");
        existingTimeSlot.setGreen("Original Green");
        existingTimeSlot.setAmber("Original Amber");
        existingTimeSlot.setRed("Original Red");

        // Mock the repository's findById method to return the existing TimeSlot
        when(timeSlotRepository.findById("1")).thenReturn(java.util.Optional.of(existingTimeSlot));

        // Mock the repository's save method to return the updated TimeSlot
        when(timeSlotRepository.save(any(TimeSlot.class))).thenAnswer(invocation -> invocation.<TimeSlot>getArgument(0));

        // Call the service method to update the TimeSlot
        TimeSlotResponseDTO result = timeSlotService.updateTimeSlot("1", inputDTO);

        // Verify that the service method returns the updated DTO
        assertEquals("1", result.getId());
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Amber", result.getAmber());

        // Verify that the repository's findById method was called with the correct ID
        verify(timeSlotRepository, times(1)).findById("1");

        // Verify that the repository's save method was called with the correct TimeSlot object
        verify(timeSlotRepository, times(1)).save(argThat(argument -> {
            // Verify that the TimeSlot argument passed to save matches the inputDTO
            return "Updated Title".equals(argument.getTitle()) &&
                    "Updated Description".equals(argument.getDescription()) &&
                    "Updated Green".equals(argument.getGreen()) &&
                    "Updated Amber".equals(argument.getAmber()) &&
                    "Updated Red".equals(argument.getRed());
        }));
    }

    @Test
    void testUpdateTimeSlotNotFound() {
        // Create a sample TimeSlotWithoutIdDTO for update
        TimeSlotWithoutIdDTO inputDTO = new TimeSlotWithoutIdDTO();
        inputDTO.setTitle("Updated Title");

        // Mock the repository's findById method to return an empty optional
        when(timeSlotRepository.findById("1")).thenReturn(java.util.Optional.empty());

        // Verify that the TimeSlotNotFoundException is thrown when updating a non-existent TimeSlot
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.updateTimeSlot( "1", inputDTO));

        // Verify that the repository's findById method was called with the correct ID
        verify(timeSlotRepository, times(1)).findById("1");

        // Verify that the repository's save method was never called
        verify(timeSlotRepository, never()).save(any(TimeSlot.class));
    }

    @Test
    void testDeleteTimeSlot() {
        // Create a mock TimeSlot for an existing record in the database
        TimeSlot existingTimeSlot = new TimeSlot();
        existingTimeSlot.setId("1");
        existingTimeSlot.setTitle("Title");
        existingTimeSlot.setDescription("Description");
        existingTimeSlot.setGreen("Green");
        existingTimeSlot.setAmber("Amber");
        existingTimeSlot.setRed("Red");

        // Mock the repository's findById method to return the existing TimeSlot
        when(timeSlotRepository.findById("1")).thenReturn(java.util.Optional.of(existingTimeSlot));

        // Call the service method to delete the TimeSlot
        timeSlotService.deleteTimeSlot("1");

        // Verify that the repository's findById method was called with the correct ID
        verify(timeSlotRepository, times(1)).findById("1");

        // Verify that the repository's delete method was called with the correct TimeSlot object
        verify(timeSlotRepository, times(1)).delete(existingTimeSlot);
    }

    @Test
    void testDeleteTimeSlotNotFound() {
        // Mock the repository's findById method to return an empty optional
        when(timeSlotRepository.findById("1")).thenReturn(java.util.Optional.empty());

        // Verify that the TimeSlotNotFoundException is thrown when deleting a non-existent TimeSlot
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.deleteTimeSlot("1"));

        // Verify that the repository's findById method was called with the correct ID
        verify(timeSlotRepository, times(1)).findById("1");

        // Verify that the repository's delete method was never called
        verify(timeSlotRepository, never()).delete(any(TimeSlot.class));
    }
    @Test
    void testGetTimeSlotByTitleAndRed() {
        // Create a mock TimeSlot object with the given title and red values
        String title = "Title";
        String red = "Red";
        TimeSlot mockTimeSlot = new TimeSlot();
        mockTimeSlot.setId("1");
        mockTimeSlot.setTitle(title);
        mockTimeSlot.setDescription("Description");
        mockTimeSlot.setGreen("Green");
        mockTimeSlot.setAmber("Amber");
        mockTimeSlot.setRed(red);

        // Mock the repository's findByTitleAndRed method to return the mock TimeSlot
        when(timeSlotRepository.findByTitleAndRed(title, red)).thenReturn(Optional.of(mockTimeSlot));

        // Call the service method to get the TimeSlot by title and red
        TimeSlotResponseDTO result = timeSlotService.getTimeSlotByTitleAndRed(title, red);

        // Verify that the repository's findByTitleAndRed method was called with the correct parameters
        verify(timeSlotRepository, times(1)).findByTitleAndRed(title, red);

        // Verify that the result matches the mock TimeSlot
        assertEquals(mockTimeSlot.getId(), result.getId());
        assertEquals(mockTimeSlot.getTitle(), result.getTitle());
        assertEquals(mockTimeSlot.getDescription(), result.getDescription());
        assertEquals(mockTimeSlot.getGreen(), result.getGreen());
        assertEquals(mockTimeSlot.getAmber(), result.getAmber());
        assertEquals(mockTimeSlot.getRed(), result.getRed());
    }

    @Test
    void testGetTimeSlotByTitleAndRedNotFound() {
        // Mock the repository's findByTitleAndRed method to return an empty optional
        when(timeSlotRepository.findByTitleAndRed("Title", "Red")).thenReturn(Optional.empty());

        // Verify that the TimeSlotNotFoundException is thrown when the TimeSlot is not found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotByTitleAndRed("Title", "Red"));

        // Verify that the repository's findByTitleAndRed method was called with the correct parameters
        verify(timeSlotRepository, times(1)).findByTitleAndRed("Title", "Red");
    }

    @Test
    void testGetTimeSlotByTitleAndDescription() {
        // Create a mock TimeSlot object with the given title and description values
        String title = "Title";
        String description = "Description";
        TimeSlot mockTimeSlot = new TimeSlot();
        mockTimeSlot.setId("1");
        mockTimeSlot.setTitle(title);
        mockTimeSlot.setDescription(description);
        mockTimeSlot.setGreen("Green");
        mockTimeSlot.setAmber("Amber");
        mockTimeSlot.setRed("Red");

        // Mock the repository's findByTitleAndDescription method to return the mock TimeSlot
        when(timeSlotRepository.findByTitleAndDescription(title, description)).thenReturn(Optional.of(mockTimeSlot));

        // Call the service method to get the TimeSlot by title and description
        TimeSlotResponseDTO result = timeSlotService.getTimeSlotByTitleAndDescription(title, description);

        // Verify that the repository's findByTitleAndDescription method was called with the correct parameters
        verify(timeSlotRepository, times(1)).findByTitleAndDescription(title, description);

        // Verify that the result matches the mock TimeSlot
        assertEquals(mockTimeSlot.getId(), result.getId());
        assertEquals(mockTimeSlot.getTitle(), result.getTitle());
        assertEquals(mockTimeSlot.getDescription(), result.getDescription());
        assertEquals(mockTimeSlot.getGreen(), result.getGreen());
        assertEquals(mockTimeSlot.getAmber(), result.getAmber());
        assertEquals(mockTimeSlot.getRed(), result.getRed());
    }

    @Test
    void testGetTimeSlotByTitleAndDescriptionNotFound() {
        // Mock the repository's findByTitleAndDescription method to return an empty optional
        when(timeSlotRepository.findByTitleAndDescription("Title", "Description")).thenReturn(Optional.empty());

        // Verify that the TimeSlotNotFoundException is thrown when the TimeSlot is not found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotByTitleAndDescription("Title", "Description"));

        // Verify that the repository's findByTitleAndDescription method was called with the correct parameters
        verify(timeSlotRepository, times(1)).findByTitleAndDescription("Title", "Description");
    }

    @Test
    void testGetTimeSlotsByTitle() {
        // Create a list of mock TimeSlot objects with the same title
        String title = "Title";
        TimeSlot mockTimeSlot1 = new TimeSlot();
        mockTimeSlot1.setId("1");
        mockTimeSlot1.setTitle(title);
        mockTimeSlot1.setDescription("Description 1");
        mockTimeSlot1.setGreen("Green 1");
        mockTimeSlot1.setAmber("Amber 1");
        mockTimeSlot1.setRed("Red 1");

        TimeSlot mockTimeSlot2 = new TimeSlot();
        mockTimeSlot2.setId("2");
        mockTimeSlot2.setTitle(title);
        mockTimeSlot2.setDescription("Description 2");
        mockTimeSlot2.setGreen("Green 2");
        mockTimeSlot2.setAmber("Amber 2");
        mockTimeSlot2.setRed("Red 2");

        List<TimeSlot> mockTimeSlots = Arrays.asList(mockTimeSlot1, mockTimeSlot2);

        // Mock the repository's findAllByTitleEqualsIgnoreCase method to return the mock TimeSlots
        when(timeSlotRepository.findAllByTitleEqualsIgnoreCase(title)).thenReturn(mockTimeSlots);

        // Call the service method to get TimeSlots by title
        List<TimeSlotResponseDTO> result = timeSlotService.getTimeSlotsByTitle(title);

        // Verify that the repository's findAllByTitleEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByTitleEqualsIgnoreCase(title);

        // Verify that the result contains the correct number of TimeSlotResponseDTO objects
        assertEquals(mockTimeSlots.size(), result.size());

        // Verify that each TimeSlotResponseDTO in the result matches the corresponding mock TimeSlot
        for (int i = 0; i < mockTimeSlots.size(); i++) {
            TimeSlot mockTimeSlot = mockTimeSlots.get(i);
            TimeSlotResponseDTO responseDTO = result.get(i);
            assertEquals(mockTimeSlot.getId(), responseDTO.getId());
            assertEquals(mockTimeSlot.getTitle(), responseDTO.getTitle());
            assertEquals(mockTimeSlot.getDescription(), responseDTO.getDescription());
            assertEquals(mockTimeSlot.getGreen(), responseDTO.getGreen());
            assertEquals(mockTimeSlot.getAmber(), responseDTO.getAmber());
            assertEquals(mockTimeSlot.getRed(), responseDTO.getRed());
        }
    }

    @Test
    void testGetTimeSlotsByTitleNotFound() {
        // Mock the repository's findAllByTitleEqualsIgnoreCase method to return an empty list
        when(timeSlotRepository.findAllByTitleEqualsIgnoreCase("NonExistentTitle")).thenReturn(List.of());

        // Verify that the TimeSlotNotFoundException is thrown when no TimeSlots are found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotsByTitle("NonExistentTitle"));

        // Verify that the repository's findAllByTitleEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByTitleEqualsIgnoreCase("NonExistentTitle");
    }

    @Test
    void testGetTimeSlotsByDescription() {
        // Create a list of mock TimeSlot objects with the same description
        String description = "Description";
        TimeSlot mockTimeSlot1 = new TimeSlot();
        mockTimeSlot1.setId("1");
        mockTimeSlot1.setTitle("Title 1");
        mockTimeSlot1.setDescription(description);
        mockTimeSlot1.setGreen("Green 1");
        mockTimeSlot1.setAmber("Amber 1");
        mockTimeSlot1.setRed("Red 1");

        TimeSlot mockTimeSlot2 = new TimeSlot();
        mockTimeSlot2.setId("2");
        mockTimeSlot2.setTitle("Title 2");
        mockTimeSlot2.setDescription(description);
        mockTimeSlot2.setGreen("Green 2");
        mockTimeSlot2.setAmber("Amber 2");
        mockTimeSlot2.setRed("Red 2");

        List<TimeSlot> mockTimeSlots = Arrays.asList(mockTimeSlot1, mockTimeSlot2);

        // Mock the repository's findAllByDescriptionEqualsIgnoreCase method to return the mock TimeSlots
        when(timeSlotRepository.findAllByDescriptionEqualsIgnoreCase(description)).thenReturn(mockTimeSlots);

        // Call the service method to get TimeSlots by description
        List<TimeSlotResponseDTO> result = timeSlotService.getTimeSlotsByDescription(description);

        // Verify that the repository's findAllByDescriptionEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByDescriptionEqualsIgnoreCase(description);

        // Verify that the result contains the correct number of TimeSlotResponseDTO objects
        assertEquals(mockTimeSlots.size(), result.size());

        // Verify that each TimeSlotResponseDTO in the result matches the corresponding mock TimeSlot
        for (int i = 0; i < mockTimeSlots.size(); i++) {
            TimeSlot mockTimeSlot = mockTimeSlots.get(i);
            TimeSlotResponseDTO responseDTO = result.get(i);
            assertEquals(mockTimeSlot.getId(), responseDTO.getId());
            assertEquals(mockTimeSlot.getTitle(), responseDTO.getTitle());
            assertEquals(mockTimeSlot.getDescription(), responseDTO.getDescription());
            assertEquals(mockTimeSlot.getGreen(), responseDTO.getGreen());
            assertEquals(mockTimeSlot.getAmber(), responseDTO.getAmber());
            assertEquals(mockTimeSlot.getRed(), responseDTO.getRed());
        }
    }

    @Test
    void testGetTimeSlotsByDescriptionNotFound() {
        // Mock the repository's findAllByDescriptionEqualsIgnoreCase method to return an empty list
        when(timeSlotRepository.findAllByDescriptionEqualsIgnoreCase("NonExistentDescription")).thenReturn(List.of());

        // Verify that the TimeSlotNotFoundException is thrown when no TimeSlots are found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotsByDescription("NonExistentDescription"));

        // Verify that the repository's findAllByDescriptionEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByDescriptionEqualsIgnoreCase("NonExistentDescription");
    }

    @Test
    void testGetTimeSlotsByGreen() {
        // Create a list of mock TimeSlot objects with the same green value
        String green = "Green Value";
        TimeSlot mockTimeSlot1 = new TimeSlot();
        mockTimeSlot1.setId("1");
        mockTimeSlot1.setTitle("Title 1");
        mockTimeSlot1.setDescription("Description 1");
        mockTimeSlot1.setGreen(green);
        mockTimeSlot1.setAmber("Amber 1");
        mockTimeSlot1.setRed("Red 1");

        TimeSlot mockTimeSlot2 = new TimeSlot();
        mockTimeSlot2.setId("2");
        mockTimeSlot2.setTitle("Title 2");
        mockTimeSlot2.setDescription("Description 2");
        mockTimeSlot2.setGreen(green);
        mockTimeSlot2.setAmber("Amber 2");
        mockTimeSlot2.setRed("Red 2");

        List<TimeSlot> mockTimeSlots = Arrays.asList(mockTimeSlot1, mockTimeSlot2);

        // Mock the repository's findAllByGreenEqualsIgnoreCase method to return the mock TimeSlots
        when(timeSlotRepository.findAllByGreenEqualsIgnoreCase(green)).thenReturn(mockTimeSlots);

        // Call the service method to get TimeSlots by green
        List<TimeSlotResponseDTO> result = timeSlotService.getTimeSlotsByGreen(green);

        // Verify that the repository's findAllByGreenEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByGreenEqualsIgnoreCase(green);

        // Verify that the result contains the correct number of TimeSlotResponseDTO objects
        assertEquals(mockTimeSlots.size(), result.size());

        // Verify that each TimeSlotResponseDTO in the result matches the corresponding mock TimeSlot
        for (int i = 0; i < mockTimeSlots.size(); i++) {
            TimeSlot mockTimeSlot = mockTimeSlots.get(i);
            TimeSlotResponseDTO responseDTO = result.get(i);
            assertEquals(mockTimeSlot.getId(), responseDTO.getId());
            assertEquals(mockTimeSlot.getTitle(), responseDTO.getTitle());
            assertEquals(mockTimeSlot.getDescription(), responseDTO.getDescription());
            assertEquals(mockTimeSlot.getGreen(), responseDTO.getGreen());
            assertEquals(mockTimeSlot.getAmber(), responseDTO.getAmber());
            assertEquals(mockTimeSlot.getRed(), responseDTO.getRed());
        }
    }

    @Test
    void testGetTimeSlotsByGreenNotFound() {
        // Mock the repository's findAllByGreenEqualsIgnoreCase method to return an empty list
        when(timeSlotRepository.findAllByGreenEqualsIgnoreCase("NonExistentGreen")).thenReturn(List.of());

        // Verify that the TimeSlotNotFoundException is thrown when no TimeSlots are found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotsByGreen("NonExistentGreen"));

        // Verify that the repository's findAllByGreenEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByGreenEqualsIgnoreCase("NonExistentGreen");
    }

    @Test
    void testGetTimeSlotsByAmber() {
        // Create a list of mock TimeSlot objects with the same amber value
        String amber = "Amber Value";
        TimeSlot mockTimeSlot1 = new TimeSlot();
        mockTimeSlot1.setId("1");
        mockTimeSlot1.setTitle("Title 1");
        mockTimeSlot1.setDescription("Description 1");
        mockTimeSlot1.setGreen("Green 1");
        mockTimeSlot1.setAmber(amber);
        mockTimeSlot1.setRed("Red 1");

        TimeSlot mockTimeSlot2 = new TimeSlot();
        mockTimeSlot2.setId("2");
        mockTimeSlot2.setTitle("Title 2");
        mockTimeSlot2.setDescription("Description 2");
        mockTimeSlot2.setGreen("Green 2");
        mockTimeSlot2.setAmber(amber);
        mockTimeSlot2.setRed("Red 2");

        List<TimeSlot> mockTimeSlots = Arrays.asList(mockTimeSlot1, mockTimeSlot2);

        // Mock the repository's findAllByAmberEqualsIgnoreCase method to return the mock TimeSlots
        when(timeSlotRepository.findAllByAmberEqualsIgnoreCase(amber)).thenReturn(mockTimeSlots);

        // Call the service method to get TimeSlots by amber
        List<TimeSlotResponseDTO> result = timeSlotService.getTimeSlotsByAmber(amber);

        // Verify that the repository's findAllByAmberEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByAmberEqualsIgnoreCase(amber);

        // Verify that the result contains the correct number of TimeSlotResponseDTO objects
        assertEquals(mockTimeSlots.size(), result.size());

        // Verify that each TimeSlotResponseDTO in the result matches the corresponding mock TimeSlot
        for (int i = 0; i < mockTimeSlots.size(); i++) {
            TimeSlot mockTimeSlot = mockTimeSlots.get(i);
            TimeSlotResponseDTO responseDTO = result.get(i);
            assertEquals(mockTimeSlot.getId(), responseDTO.getId());
            assertEquals(mockTimeSlot.getTitle(), responseDTO.getTitle());
            assertEquals(mockTimeSlot.getDescription(), responseDTO.getDescription());
            assertEquals(mockTimeSlot.getGreen(), responseDTO.getGreen());
            assertEquals(mockTimeSlot.getAmber(), responseDTO.getAmber());
            assertEquals(mockTimeSlot.getRed(), responseDTO.getRed());
        }
    }

    @Test
    void testGetTimeSlotsByAmberNotFound() {
        // Mock the repository's findAllByAmberEqualsIgnoreCase method to return an empty list
        when(timeSlotRepository.findAllByAmberEqualsIgnoreCase("NonExistentAmber")).thenReturn(List.of());

        // Verify that the TimeSlotNotFoundException is thrown when no TimeSlots are found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotsByAmber("NonExistentAmber"));

        // Verify that the repository's findAllByAmberEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByAmberEqualsIgnoreCase("NonExistentAmber");
    }

    @Test
    void testGetTimeSlotsByRed() {
        // Create a list of mock TimeSlot objects with the same red value
        String red = "Red Value";
        TimeSlot mockTimeSlot1 = new TimeSlot();
        mockTimeSlot1.setId("1");
        mockTimeSlot1.setTitle("Title 1");
        mockTimeSlot1.setDescription("Description 1");
        mockTimeSlot1.setGreen("Green 1");
        mockTimeSlot1.setAmber("Amber 1");
        mockTimeSlot1.setRed(red);

        TimeSlot mockTimeSlot2 = new TimeSlot();
        mockTimeSlot2.setId("2");
        mockTimeSlot2.setTitle("Title 2");
        mockTimeSlot2.setDescription("Description 2");
        mockTimeSlot2.setGreen("Green 2");
        mockTimeSlot2.setAmber("Amber 2");
        mockTimeSlot2.setRed(red);

        List<TimeSlot> mockTimeSlots = Arrays.asList(mockTimeSlot1, mockTimeSlot2);

        // Mock the repository's findAllByRedEqualsIgnoreCase method to return the mock TimeSlots
        when(timeSlotRepository.findAllByRedEqualsIgnoreCase(red)).thenReturn(mockTimeSlots);

        // Call the service method to get TimeSlots by red
        List<TimeSlotResponseDTO> result = timeSlotService.getTimeSlotsByRed(red);

        // Verify that the repository's findAllByRedEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByRedEqualsIgnoreCase(red);

        // Verify that the result contains the correct number of TimeSlotResponseDTO objects
        assertEquals(mockTimeSlots.size(), result.size());

        // Verify that each TimeSlotResponseDTO in the result matches the corresponding mock TimeSlot
        for (int i = 0; i < mockTimeSlots.size(); i++) {
            TimeSlot mockTimeSlot = mockTimeSlots.get(i);
            TimeSlotResponseDTO responseDTO = result.get(i);
            assertEquals(mockTimeSlot.getId(), responseDTO.getId());
            assertEquals(mockTimeSlot.getTitle(), responseDTO.getTitle());
            assertEquals(mockTimeSlot.getDescription(), responseDTO.getDescription());
            assertEquals(mockTimeSlot.getGreen(), responseDTO.getGreen());
            assertEquals(mockTimeSlot.getAmber(), responseDTO.getAmber());
            assertEquals(mockTimeSlot.getRed(), responseDTO.getRed());
        }
    }

    @Test
    void testGetTimeSlotsByRedNotFound() {
        // Mock the repository's findAllByRedEqualsIgnoreCase method to return an empty list
        when(timeSlotRepository.findAllByRedEqualsIgnoreCase("NonExistentRed")).thenReturn(List.of());

        // Verify that the TimeSlotNotFoundException is thrown when no TimeSlots are found
        assertThrows(TimeSlotNotFoundException.class, () -> timeSlotService.getTimeSlotsByRed("NonExistentRed"));

        // Verify that the repository's findAllByRedEqualsIgnoreCase method was called with the correct parameter
        verify(timeSlotRepository, times(1)).findAllByRedEqualsIgnoreCase("NonExistentRed");
    }
}