package de.iav.frontend.model;


public record UserWithoutIdDto(
        String firstName,
        String lastName,
        String email,
        String password

) {
}
