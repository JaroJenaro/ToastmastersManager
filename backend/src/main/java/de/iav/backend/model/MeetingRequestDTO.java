package de.iav.backend.model;

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
    String dateTime;
    String location;
    List<SpeechContributionDTO> speechContributionList;
}