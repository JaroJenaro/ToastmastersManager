package de.iav.backend.service;

import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.model.TimeSlot;
import de.iav.backend.model.TimeSlotResponseDTO;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.iav.backend.repository.TimeSlotRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private static final String WAS_NOT_FOUND = " was not found.";

    private final TimeSlotRepository timeSlotRepository;

    public List<TimeSlotResponseDTO> getAllTimeSlots(){
        return timeSlotRepository.findAll()
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public TimeSlotResponseDTO getTimeSlotById(String id){

        TimeSlot timeSlot = timeSlotRepository.
                findById(id).
                orElseThrow(() -> new TimeSlotNotFoundException(id));

        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
    }

    public TimeSlotResponseDTO addTimeSlot(TimeSlotWithoutIdDTO timeSlotWithoutIdDTO) {
        TimeSlot timeSlot = TimeSlot.builder()
                .title(timeSlotWithoutIdDTO.getTitle())
                .description(timeSlotWithoutIdDTO.getDescription())
                .green(timeSlotWithoutIdDTO.getGreen())
                .amber(timeSlotWithoutIdDTO.getAmber())
                .red(timeSlotWithoutIdDTO.getRed())
                .build();

        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlot);

        return TimeSlotResponseDTO.builder()
                .id(savedTimeSlot.getId())
                .title(savedTimeSlot.getTitle())
                .description(savedTimeSlot.getDescription())
                .green(savedTimeSlot.getGreen())
                .amber(savedTimeSlot.getAmber())
                .red(savedTimeSlot.getRed())
                .build();
    }

    public TimeSlotResponseDTO updateTimeSlot(TimeSlotWithoutIdDTO timeSlotWithoutIdDTO, String id) {
        TimeSlot timeSlotToUpdate = timeSlotRepository
                .findById(id)
                .orElseThrow(() -> new TimeSlotNotFoundException(id));
        timeSlotToUpdate.setTitle(timeSlotWithoutIdDTO.getTitle());
        timeSlotToUpdate.setDescription(timeSlotWithoutIdDTO.getDescription());
        timeSlotToUpdate.setGreen(timeSlotWithoutIdDTO.getGreen());
        timeSlotToUpdate.setAmber(timeSlotWithoutIdDTO.getAmber());
        timeSlotToUpdate.setRed(timeSlotWithoutIdDTO.getRed());

        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlotToUpdate);

        return TimeSlotResponseDTO.builder()
                .id(savedTimeSlot.getId())
                .title(savedTimeSlot.getTitle())
                .description(savedTimeSlot.getDescription())
                .green(savedTimeSlot.getGreen())
                .amber(savedTimeSlot.getAmber())
                .red(savedTimeSlot.getRed())
                .build();
    }
    public void deleteTimeSlot(String id) {
        TimeSlot timeSlot = timeSlotRepository
                .findById(id)
                .orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot with id  " + id + WAS_NOT_FOUND));
        timeSlotRepository.delete(timeSlot);
    }

    public TimeSlotResponseDTO getTimeSlotByTitleAndRed(String title, String red) {

        TimeSlot timeSlot = timeSlotRepository.
                findByTitleAndRed(title, red).
                orElseThrow(() -> new TimeSlotNotFoundException("TimeSlo with title " + title + " red time " + red + WAS_NOT_FOUND));

        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
    }

    public TimeSlotResponseDTO getTimeSlotByTitleAndDescription(String title, String description) {
        TimeSlot timeSlot = timeSlotRepository.
                findByTitleAndDescription(title, description).
                orElseThrow(() -> new TimeSlotNotFoundException("TimeSlo with title " + title + " description " + description + WAS_NOT_FOUND));

        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByTitle(String titel) {
        List<TimeSlot> timeSlots = timeSlotRepository
                .findAllByTitleEqualsIgnoreCase(titel);
        if (timeSlots.isEmpty())
            throw new TimeSlotNotFoundException("TimeSlots with Titel " + titel + WAS_NOT_FOUND);

        return timeSlots
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByDescription(String description) {
       List<TimeSlot> timeSlots = timeSlotRepository
                .findAllByDescriptionEqualsIgnoreCase(description);
        if (timeSlots.isEmpty())
            throw new TimeSlotNotFoundException("TimeSlots with description " + description + WAS_NOT_FOUND);

        return timeSlots
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByGreen(String green) {
        List<TimeSlot> timeSlots = timeSlotRepository
                .findAllByGreenEqualsIgnoreCase(green);
        if (timeSlots.isEmpty())
            throw new TimeSlotNotFoundException("TimeSlots with green " + green + WAS_NOT_FOUND);

        return timeSlots
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByAmber(String amber) {
        List<TimeSlot> timeSlots = timeSlotRepository
                .findAllByAmberEqualsIgnoreCase(amber);
        if (timeSlots.isEmpty())
            throw new TimeSlotNotFoundException("TimeSlots with amber " + amber + WAS_NOT_FOUND);

        return timeSlots
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByRed(String red) {
        List<TimeSlot> timeSlots = timeSlotRepository
                .findAllByRedEqualsIgnoreCase(red);
        if (timeSlots.isEmpty())
            throw new TimeSlotNotFoundException("TimeSlots with red " + red + WAS_NOT_FOUND);

        return timeSlots

                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public TimeSlotResponseDTO getTimeSlotResponseDTO(TimeSlot timeSlot){
        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
    }
}