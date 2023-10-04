package de.iav.backend.service;


import de.iav.backend.exception.UserNotFoundException;
import de.iav.backend.model.User;
import de.iav.backend.model.UserResponseDTO;
import de.iav.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public final List<User> tempUsers = new ArrayList<>(Arrays.asList(
            new User(null, "Erum", "Schuakat", "erum.schaukat@iav.de", "12345", "USER"),
            new User(null, "Houman", "Mohammadi", "houman.mohammadi@iav.de", "23456", "USER"),
            new User(null, "Jaroslaw", "Placzek", "jaroslaw.placzek@iav.de", "34567", "USER")
    ));
    private final UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public UserResponseDTO updateUser(String id, UserResponseDTO userResponseDtoToUpdate) {
        User userToUpdate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userToUpdate.setFirstName(userResponseDtoToUpdate.getFirstName());
        userToUpdate.setLastName(userResponseDtoToUpdate.getLastName());
        userToUpdate.setEmail(userResponseDtoToUpdate.getEmail());
        userToUpdate.setRole(userResponseDtoToUpdate.getRole());

        User savedUser = userRepository.save(userToUpdate);


        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();


    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public List<User> setUserByRepository() {
        if (userRepository.findAll().isEmpty())
            return fillDataWithUsers();
        else
            return userRepository.findAll();
    }

    private List<User> fillDataWithUsers() {
        return userRepository.saveAll(tempUsers);
    }

    public Optional<UserResponseDTO> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email).getUserWithoutUserDetails());
    }
}