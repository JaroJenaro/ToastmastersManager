package de.iav.backend.controller;

import de.iav.backend.model.UserResponseDTO;

import de.iav.backend.security.NewAppUser;
import de.iav.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/toast-master-manager/users")
public class UserController {

    private final UserService userService;

      @GetMapping
    public List<UserResponseDTO> getAllTimeSlots(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String role) {

        if (firstName != null) {
            return userService.getUsersByFirstName(firstName);}
        else if (lastName != null) {
            return userService.getUsersByLastName(lastName);}
        else if (email != null) {
            return userService.getUsersByEmail(email);}
        else if (role != null) {
            return userService.getUsersByRole(role);}
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("/search")
    public UserResponseDTO searchUserByFirstNameAndLastName(
            @RequestParam String firstName,
            @RequestParam String lastName) {
        return userService.getUserByFirstNameAndLastName(firstName, lastName);
    }

    @GetMapping("/search2")
    public UserResponseDTO searchUserByFirstNameAndEmail(
            @RequestParam String firstName,
            @RequestParam String email) {
        return userService.getUserByFirstNameAndEmail(firstName, email);
    }
    @GetMapping("/email/{email}")
    public UserResponseDTO getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody NewAppUser newAppUser) {
        UserResponseDTO createdUser = userService.register(newAppUser);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable String id, @Valid @RequestBody NewAppUser user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}