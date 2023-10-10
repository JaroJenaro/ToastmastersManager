package de.iav.frontend.model;


public record UserRequestDto(
        String firstName,
        String lastName,
        String email,
        String password

) {
}
