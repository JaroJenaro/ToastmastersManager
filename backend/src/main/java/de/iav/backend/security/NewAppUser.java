package de.iav.backend.security;

public record NewAppUser(
        String username,
        String firstName,
        String lastName,
        String password,
        String email
) {


}