package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserWithoutUserDetails {
    String id;
    String firstName;
    String lastName;
    String email;
    String role;
}
