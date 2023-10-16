package de.iav.backend.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SpeechContributionIn {
    @NotNull
    TimeSlotResponseDTO timeSlot;
    UserResponseDTO user;
    String stoppedTime;
}