package de.iav.frontend.security;

public record AppUserRequest(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
