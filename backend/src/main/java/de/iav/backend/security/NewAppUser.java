package de.iav.backend.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewAppUser(
        String username,
        @NotNull
        @NotBlank(message = "firstName is required")
        @Size(min = 2, message = "firstName must be at least 2 characters long")
        String firstName,
        @NotNull
        @NotBlank(message = "lastName is required")
        @Size(min = 2, message = "lastName must be at least 2 characters long")
        String lastName,
        String password,
        @NotNull
        @NotBlank(message = "email is required")
        @Size(min = 2, message = "email must be at least 2 characters long")
        String email
) {


}