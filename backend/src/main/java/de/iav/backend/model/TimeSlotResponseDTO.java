package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TimeSlotResponseDTO {
    String id;
    String title;
    String description;
    String green;
    String amber;
    String red;
}