package de.iav.backend.service;


import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.exception.UserNotFoundException;
import de.iav.backend.model.User;
import de.iav.backend.model.UserResponseDTO;
import de.iav.backend.repository.UserRepository;


import de.iav.backend.security.NewAppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class UserService {
    private static final String WAS_NOT_FOUND = " was not found.";
       private final UserRepository userRepository;


    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.
                findById(id).
                orElseThrow(() -> new TimeSlotNotFoundException("USER with id " + id + WAS_NOT_FOUND));

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
    public void deleteUser(String id){
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id  " + id + WAS_NOT_FOUND));
        userRepository.delete(user);
    }

    public UserResponseDTO updateUser(String id, NewAppUser userDto) {
        User userToUpdate = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id  " + id + WAS_NOT_FOUND));
        userToUpdate.setFirstName(userDto.firstName());
        userToUpdate.setLastName(userDto.lastName());
        userToUpdate.setEmail(userDto.email());


        User savedUser = userRepository.save(userToUpdate);


        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();


    }

    public UserResponseDTO register(NewAppUser newAppUser) {

        Argon2PasswordEncoder passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        User user = new User(
                null,
                newAppUser.firstName(),
                newAppUser.lastName(),
                newAppUser.email(),
                passwordEncoder.encode(newAppUser.password()),
                "USER"
        );
        User savedUser = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    public UserResponseDTO getUserByEmail(String email) {

        User user = userRepository.
                findByEmail(email).
                orElseThrow(() -> new TimeSlotNotFoundException("USER with email " + email + WAS_NOT_FOUND));
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

    }

    public List<UserResponseDTO> getUsersByFirstName(String searchString) {

        List<User> users = userRepository
                .findAllByFirstNameEqualsIgnoreCase(searchString);

        if (users.isEmpty())
            throw new TimeSlotNotFoundException("Users with FirstName: " + searchString + WAS_NOT_FOUND);

        return users
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }

    public List<UserResponseDTO> getUsersByLastName(String searchString) {
        List<User> users = userRepository
                .findAllByLastNameEqualsIgnoreCase(searchString);

        if (users.isEmpty())
            throw new TimeSlotNotFoundException("Users with LastName: " + searchString + WAS_NOT_FOUND);

        return users
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }
    public List<UserResponseDTO> getUsersByEmail(String searchString) {
        List<User> users = userRepository
                .findAllByEmailEqualsIgnoreCase(searchString);
        if (users.isEmpty())
            throw new TimeSlotNotFoundException("Users with Email: " + searchString + WAS_NOT_FOUND);
        return users
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }
    public List<UserResponseDTO> getUsersByRole(String searchString) {
        List<User> users = userRepository
                .findAllByRoleEqualsIgnoreCase(searchString);
        if (users.isEmpty())
            throw new TimeSlotNotFoundException("Users with Role: " + searchString + WAS_NOT_FOUND);
        return users
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build())
                .toList();
    }

    public UserResponseDTO getUserByFirstNameAndLastName(String firstName, String lastName) {
        User user = userRepository.
                findUserByFirstNameEqualsIgnoreCaseAndLastNameEqualsIgnoreCase(firstName, lastName).
                orElseThrow(() -> new TimeSlotNotFoundException("USER with firstName " + firstName  + " and  lastName " + lastName +  WAS_NOT_FOUND));
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public UserResponseDTO getUserByFirstNameAndEmail(String firstName, String email) {
        User user = userRepository.
                findUserByFirstNameEqualsIgnoreCaseAndEmailEqualsIgnoreCase(firstName, email).
                orElseThrow(() -> new TimeSlotNotFoundException("USER with firstName " + firstName  + " and  Email: " + email +  WAS_NOT_FOUND));
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}