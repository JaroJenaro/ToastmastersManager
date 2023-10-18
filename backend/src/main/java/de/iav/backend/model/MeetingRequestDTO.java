package de.iav.backend.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MeetingRequestDTO {
    @NotNull
    String dateTime;
    @NotNull
    String location;
    List<SpeechContributionDTO> speechContributionList;
}