package de.iav.frontend.model;


public record User(
        String id,
        String firstName,
        String lastName,
        String email,
        Role role

) {
}
