package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SpeechContributionIn {
    TimeSlotResponseDTO timeSlotDto;
    UserResponseDTO userDto;
    String stoppedTime;
}