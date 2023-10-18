package de.iav.backend.util;

import de.iav.backend.model.*;

public class BackendBuilder {

    BackendBuilder() {
    }
    //BackendBuilder.getTimeSlotResponseDTO

    public static SpeechContribution getSpeechContributionFromDTO(SpeechContributionDTO speechContributionDto){
            return SpeechContribution.builder()
                    .id(speechContributionDto.getId())
                    .timeSlot(TimeSlot.builder()
                            .id(speechContributionDto.getTimeSlot().getId())
                            .title(speechContributionDto.getTimeSlot().getTitle())
                            .description(speechContributionDto.getTimeSlot().getDescription())
                            .green(speechContributionDto.getTimeSlot().getGreen())
                            .amber(speechContributionDto.getTimeSlot().getAmber())
                            .red(speechContributionDto.getTimeSlot().getRed())
                            .build())
                    .user(speechContributionDto.getUser() != null ? User.builder()
                            .id(speechContributionDto.getUser().getId())
                            .firstName(speechContributionDto.getUser().getFirstName())
                            .lastName(speechContributionDto.getUser().getLastName())
                            .email(speechContributionDto.getUser().getEmail())
                            .role(speechContributionDto.getUser().getRole())
                            .build(): null)
                    .stoppedTime(speechContributionDto.getStoppedTime())
                    .build();
        }

    public static  SpeechContributionDTO getSpeechContributionDTO(SpeechContribution speechContribution){
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
                .user(speechContribution.getUser() != null ? UserResponseDTO.builder()
                        .id(speechContribution.getUser().getId())
                        .firstName(speechContribution.getUser().getFirstName())
                        .lastName(speechContribution.getUser().getLastName())
                        .email(speechContribution.getUser().getEmail())
                        .role(speechContribution.getUser().getRole())
                        .build():null)
                .stoppedTime(speechContribution.getStoppedTime())
                .build();
    }

    public static SpeechContribution getSpeechContribution(SpeechContributionIn speechContributionIn){
        return SpeechContribution.builder()
                .timeSlot(TimeSlot.builder()
                        .id(speechContributionIn.getTimeSlot().getId())
                        .title(speechContributionIn.getTimeSlot().getTitle())
                        .description(speechContributionIn.getTimeSlot().getDescription())
                        .green(speechContributionIn.getTimeSlot().getGreen())
                        .amber(speechContributionIn.getTimeSlot().getAmber())
                        .red(speechContributionIn.getTimeSlot().getRed())
                        .build())
                .user(speechContributionIn.getUser() != null ? User.builder()
                        .id(speechContributionIn.getUser().getId())
                        .firstName(speechContributionIn.getUser().getFirstName())
                        .lastName(speechContributionIn.getUser().getLastName())
                        .email(speechContributionIn.getUser().getEmail())
                        .role(speechContributionIn.getUser().getRole())
                        .build():null)
                .stoppedTime(speechContributionIn.getStoppedTime())
                .build();
    }

    public static User getUserFromResponse(UserResponseDTO userResponseDTO){
        return User.builder()
                .id(userResponseDTO.getId())
                .firstName(userResponseDTO.getFirstName())
                .lastName(userResponseDTO.getLastName())
                .email(userResponseDTO.getEmail())
                .role(userResponseDTO.getRole())
                .build();
    }

    public static TimeSlotResponseDTO getTimeSlotResponseDTO(TimeSlot timeSlot) {
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