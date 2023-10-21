package de.iav.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    String id;
    @NotNull
    @NotBlank(message = "firstName is required")
    @Size(min = 2, message = "firstName must be at least 2 characters long")
    String firstName;
    @NotNull
    @NotBlank(message = "lastName is required")
    @Size(min = 2, message = "lastName must be at least 2 characters long")
    String lastName;
    @NotNull
    @NotBlank(message = "email is required")
    @Size(min = 2, message = "email must be at least 2 characters long")
    @Email
    String email;
    String role;
}
