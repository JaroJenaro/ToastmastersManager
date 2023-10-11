package de.iav.backend.service;

import de.iav.backend.exception.SpeechContributionNotFoundException;
import de.iav.backend.model.*;
import de.iav.backend.repository.SpeechContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeechContributionService {
    private static final String WAS_NOT_FOUND = " was not found.";

    public final SpeechContributionRepository speechContributionRepository;

    public List<SpeechContributionDTO> getAllSpeechContributions(){
        return speechContributionRepository.findAll()
                .stream()
                .map(speechContribution -> SpeechContributionDTO.builder()
                        .id(speechContribution.getId())
                        .userDto(UserResponseDTO.builder()
                                .id(speechContribution.getUser().getId())
                                .firstName(speechContribution.getUser().getFirstName())
                                .lastName(speechContribution.getUser().getLastName())
                                .email(speechContribution.getUser().getEmail())
                                .role(speechContribution.getUser().getRole())
                                .build())
                        .timeSlotDto(TimeSlotResponseDTO.builder()
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

    public SpeechContributionDTO addSpeechContribution(SpeechContributionIn speechContributionDTO) {
        SpeechContribution speechContribution = SpeechContribution.builder()
                .timeSlot(TimeSlot.builder()
                        .id(speechContributionDTO.getTimeSlotDto().getId())
                        .title(speechContributionDTO.getTimeSlotDto().getTitle())
                        .description(speechContributionDTO.getTimeSlotDto().getDescription())
                        .green(speechContributionDTO.getTimeSlotDto().getGreen())
                        .amber(speechContributionDTO.getTimeSlotDto().getAmber())
                        .red(speechContributionDTO.getTimeSlotDto().getRed())
                        .build())
                .user(User.builder()
                        .id(speechContributionDTO.getUserDto().getId())
                        .firstName(speechContributionDTO.getUserDto().getFirstName())
                        .lastName(speechContributionDTO.getUserDto().getLastName())
                        .email(speechContributionDTO.getUserDto().getEmail())
                        .role(speechContributionDTO.getUserDto().getRole())
                        .build())
                .stoppedTime(speechContributionDTO.getStoppedTime())
                .build();

        SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContribution);

        return getSpeechContributionDTO(savedSpeechContribution);
    }

    private SpeechContributionDTO getSpeechContributionDTO(SpeechContribution speechContribution){
        return SpeechContributionDTO.builder()
                .id(speechContribution.getId())
                .timeSlotDto(TimeSlotResponseDTO.builder()
                        .id(speechContribution.getTimeSlot().getId())
                        .title(speechContribution.getTimeSlot().getTitle())
                        .description(speechContribution.getTimeSlot().getDescription())
                        .green(speechContribution.getTimeSlot().getGreen())
                        .amber(speechContribution.getTimeSlot().getAmber())
                        .red(speechContribution.getTimeSlot().getRed())
                        .build())
                .userDto(UserResponseDTO.builder()
                        .id(speechContribution.getUser().getId())
                        .firstName(speechContribution.getUser().getFirstName())
                        .lastName(speechContribution.getUser().getLastName())
                        .email(speechContribution.getUser().getEmail())
                        .role(speechContribution.getUser().getRole())
                        .build())
                .stoppedTime(speechContribution.getStoppedTime())
                .build();
    }
}