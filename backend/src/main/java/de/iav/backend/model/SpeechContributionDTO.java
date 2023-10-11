package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpeechContributionDTO {
    String id;
    TimeSlotResponseDTO timeSlotDto;
    UserResponseDTO userDto;
    String stoppedTime;
}
