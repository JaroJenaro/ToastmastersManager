package de.iav.backend.service;

import de.iav.backend.exception.SpeechContributionBadRequestException;
import de.iav.backend.exception.SpeechContributionNotFoundException;
import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.exception.UserNotFoundException;
import de.iav.backend.model.*;
import de.iav.backend.repository.SpeechContributionRepository;
import de.iav.backend.repository.TimeSlotRepository;
import de.iav.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeechContributionService {

    private final SpeechContributionRepository speechContributionRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;

    public List<SpeechContributionDTO> getAllSpeechContributions(){
        return speechContributionRepository.findAll()
                .stream()
                .map(speechContribution -> SpeechContributionDTO.builder()
                        .id(speechContribution.getId())
                        .user(UserResponseDTO.builder()
                                .id(speechContribution.getUser().getId())
                                .firstName(speechContribution.getUser().getFirstName())
                                .lastName(speechContribution.getUser().getLastName())
                                .email(speechContribution.getUser().getEmail())
                                .role(speechContribution.getUser().getRole())
                                .build())
                        .timeSlot(TimeSlotResponseDTO.builder()
                                .id(speechContribution.getTimeSlot().getId())
                                .title(speechContribution.getTimeSlot().getTitle())
                                .description(speechContribution.getTimeSlot().getDescription())
                                .green(speechContribution.getTimeSlot().getGreen())
                                .amber(speechContribution.getTimeSlot().getAmber())
                                .red(speechContribution.getTimeSlot().getRed())
                                .build())
                        .stoppedTime(speechContribution.getStoppedTime())
                        .build())
                .toList();
    }

    public SpeechContributionDTO getSpeechContributionById(String id){
        SpeechContribution speechContribution = speechContributionRepository.
                findById(id).
                orElseThrow(() -> new SpeechContributionNotFoundException(id));

        return getSpeechContributionDTO(speechContribution);
    }

    public SpeechContributionDTO addSpeechContribution(SpeechContributionIn speechContributionIn) {
        SpeechContribution speechContribution = getSpeechContribution(speechContributionIn);

        SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContribution);

        return getSpeechContributionDTO(savedSpeechContribution);
    }

    public SpeechContributionDTO updateSpeechContribution(SpeechContributionIn speechContributionIn, String id) {
        SpeechContribution speechContributionToUpdateFromDB = speechContributionRepository
                .findById(id)
                .orElseThrow(() -> new SpeechContributionNotFoundException(id));
        // Timeslot darf nicht geändert werden oder getauscht werden
        TimeSlot timeSlotFromDB = timeSlotRepository
                .findById(speechContributionToUpdateFromDB.getTimeSlot().getId())
                .orElseThrow(() -> new TimeSlotNotFoundException(speechContributionIn.getTimeSlot().getId()));
        // User darf getauscht werden
        User userInFromDB = userRepository
                .findById(speechContributionIn.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException(speechContributionIn.getUser().getId()));
        TimeSlot timeSlotIn = getTimeSlot(speechContributionIn.getTimeSlot());
        User userIn = getUser(speechContributionIn.getUser());
        userIn.setPassword(userInFromDB.getPassword());
        if(timeSlotFromDB.equals(timeSlotIn) && userInFromDB.equals(userIn))
        {
            speechContributionToUpdateFromDB.setUser(getUser(speechContributionIn.getUser()));
            speechContributionToUpdateFromDB.setTimeSlot(getTimeSlot(speechContributionIn.getTimeSlot()));
            speechContributionToUpdateFromDB.setStoppedTime(speechContributionIn.getStoppedTime());

            SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContributionToUpdateFromDB);

            return getSpeechContributionDTO(savedSpeechContribution);
        }
        throw new SpeechContributionBadRequestException(" beim updateSpeechContribution ");
    }
    private SpeechContributionDTO getSpeechContributionDTO(SpeechContribution speechContribution){
        return SpeechContributionDTO.builder()
                .id(speechContribution.getId())
                .timeSlot(TimeSlotResponseDTO.builder()
                        .id(speechContribution.getTimeSlot().getId())
                        .title(speechContribution.getTimeSlot().getTitle())
                        .description(speechContribution.getTimeSlot().getDescription())
                        .green(speechContribution.getTimeSlot().getGreen())
                        .amber(speechContribution.getTimeSlot().getAmber())
                        .red(speechContribution.getTimeSlot().getRed())
                        .build())
                .user(UserResponseDTO.builder()
                        .id(speechContribution.getUser().getId())
                        .firstName(speechContribution.getUser().getFirstName())
                        .lastName(speechContribution.getUser().getLastName())
                        .email(speechContribution.getUser().getEmail())
                        .role(speechContribution.getUser().getRole())
                        .build())
                .stoppedTime(speechContribution.getStoppedTime())
                .build();
    }

    private SpeechContribution getSpeechContribution(SpeechContributionIn speechContributionIn){
        return SpeechContribution.builder()
                .timeSlot(TimeSlot.builder()
                        .id(speechContributionIn.getTimeSlot().getId())
                        .title(speechContributionIn.getTimeSlot().getTitle())
                        .description(speechContributionIn.getTimeSlot().getDescription())
                        .green(speechContributionIn.getTimeSlot().getGreen())
                        .amber(speechContributionIn.getTimeSlot().getAmber())
                        .red(speechContributionIn.getTimeSlot().getRed())
                        .build())
                .user(User.builder()
                        .id(speechContributionIn.getUser().getId())
                        .firstName(speechContributionIn.getUser().getFirstName())
                        .lastName(speechContributionIn.getUser().getLastName())
                        .email(speechContributionIn.getUser().getEmail())
                        .role(speechContributionIn.getUser().getRole())
                        .build())
                .stoppedTime(speechContributionIn.getStoppedTime())
                .build();
    }

    private TimeSlot getTimeSlot(TimeSlotResponseDTO timeSlotResponseDTO){
        return TimeSlot.builder()
                .id(timeSlotResponseDTO.getId())
                .title(timeSlotResponseDTO.getTitle())
                .description(timeSlotResponseDTO.getDescription())
                .green(timeSlotResponseDTO.getGreen())
                .amber(timeSlotResponseDTO.getAmber())
                .red(timeSlotResponseDTO.getRed())
                .build();
    }
    private User getUser(UserResponseDTO userResponseDTO){
        return User.builder()
                .id(userResponseDTO.getId())
                .firstName(userResponseDTO.getFirstName())
                .lastName(userResponseDTO.getLastName())
                .email(userResponseDTO.getEmail())
                .role(userResponseDTO.getRole())
                .build();
    }
}