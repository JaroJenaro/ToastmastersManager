package de.iav.backend.controller;

import de.iav.backend.model.User;
import de.iav.backend.model.UserWithoutUserDetails;
import de.iav.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
    public Optional<UserWithoutUserDetails> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/set")
    public List<User> setDefaultUsers() {
        return userService.setUserByRepository();
    }

    //ist ersetzt worden durch /register
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.saveUser(user);
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }


    /*
    *   GET http://localhost:8080/api/toastMasterManager/usersData/email/jaro.jenaro@speaker.de
        GET http://localhost:8080/api/toastMasterManager/usersData/email/jaro.jenaro@speaker.de
    * */
}