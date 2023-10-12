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

    private final SpeechContributionRepository speechContributionRepository;

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
        SpeechContribution speechContribution = SpeechContribution.builder()
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

        SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContribution);

        return getSpeechContributionDTO(savedSpeechContribution);
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
}