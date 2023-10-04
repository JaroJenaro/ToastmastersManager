package de.iav.backend.controller;

import de.iav.backend.exception.UserNotFoundException;
import de.iav.backend.model.User;
import de.iav.backend.model.UserResponseDTO;
import de.iav.backend.security.NewAppUser;
import de.iav.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/toastMasterManager/usersData")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping("email/{email}")
    public Optional<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/set")
    public List<User> setDefaultUsers() {
        return userService.setUserByRepository();
    }

    //ist ersetzt worden durch /register
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody NewAppUser newAppUser) {
        UserResponseDTO createdUser = userService.addUser(newAppUser);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable String id, @RequestBody UserResponseDTO user) {
        return userService.updateUser(id, user);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleStudentNotFoundException(UserNotFoundException exception) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", exception.getMessage());
        body.put("timestamp", Instant.now().toString());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleValidationException(BindException ex) {
        Map<String, Object> responseBody = new HashMap<>();

        for (FieldError error : ex.getFieldErrors()) {
            responseBody.put(error.getField(), error.getDefaultMessage());
            responseBody.put("timestamp", Instant.now());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}