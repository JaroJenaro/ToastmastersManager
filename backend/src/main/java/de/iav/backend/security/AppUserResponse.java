package de.iav.backend.security;

public record AppUserResponse(
        String id,

        String firstName,
        String lastName,
        String email,
        String role
) {
}