package de.iav.backend.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TimeSlotWithoutIdDTO {

    @NotNull
    @NotBlank(message = "title is required")
    @Size(min = 2, message = "title must be at least 2 characters long")
    String title;

    @NotNull
    @NotBlank(message = "description is required")
    @Size(min = 2, message = "description must be at least 2 characters long")
    String description;

    String green;

    String amber;

    @NotNull
    @NotBlank(message = "red is required")
    @Size(min = 2, message = "red must be at least 2 characters long")
    String red;
}
