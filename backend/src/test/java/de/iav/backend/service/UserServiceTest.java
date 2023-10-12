package de.iav.backend.service;

import de.iav.backend.exception.UserNotFoundException;
import de.iav.backend.model.User;
import de.iav.backend.model.UserResponseDTO;

import de.iav.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserService(userRepository);

    @Test
    void getAllUsers_whenNoUsersNotAvailable_thenReturnEmptyList() {
        // GIVEN
        List<User> expected = List.of();
        // WHEN
        when(userRepository.findAll()).thenReturn(expected);
        List<UserResponseDTO> actual = userService.getAllUsers();
        // THEN
        List<UserResponseDTO> expectedResponse = new ArrayList<>();

        assertEquals(expectedResponse, actual);
        verify(userRepository).findAll();
    }

    @Test
    void getAllUsers_whenUsersAvailable_thenReturnListOfUsers() {
        // GIVEN
        List<User> expected = List.of(
                new User("1234","Wladimir", "Putin",  "wladimir.putin@udssr.ru", "1234", "USER"),
                new User("1235","Max", "Put",  "wladi.put@udssr.ru", "1234", "USER"),
                new User("1236","Alex", "Pute",  "wlad.pute@udssr.ru", "1234", "USER")
        );

        // WHEN
        when(userRepository.findAll()).thenReturn(expected);

        List<UserResponseDTO> actual = userService.getAllUsers();
        // THEN
        List<UserResponseDTO> expectedResponse;
        expectedResponse = expected.stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())

                        .build())
                .toList();

        assertEquals(expectedResponse, actual);
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_whenUserWithGivenIdExist_thenReturnUserById() {
        // GIVEN
        //UserDTO expected = new UserDTO("1","1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00"))
        Optional<User> expected = Optional.of(new User("1","1 Speaker", "Erste vorbereitete Rede", "4:00", "5:00", "6:00"));
        // WHEN
        when(userRepository.findById("1")).thenReturn(expected);
        UserResponseDTO actual = userService.getUserById("1");
        // THEN
        assertEquals(expected.get().getId(), actual.getId());
        verify(userRepository).findById("1");
    }

    @Test
    void getUserById_whenUserWithGivenIdNotExist_thenThrowUserNotFoundException() {
        // GIVEN
        String id = "1";
        // WHEN
        when(userRepository.findById(id)).thenThrow(new UserNotFoundException(id));
        // THEN
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    void testDeleteUser() {
        // Create a mock User for an existing record in the database
        User existingUser = new User();
        existingUser.setId("1");
        existingUser.setFirstName("FirstName");
        existingUser.setLastName("LastName");
        existingUser.setEmail("Email");
        existingUser.setRole("Role");

        // Mock the repository's findById method to return the existing User
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));

        // Call the service method to delete the User
        userService.deleteUser("1");

        // Verify that the repository's findById method was called with the correct ID
        verify(userRepository, times(1)).findById("1");

        // Verify that the repository's delete method was called with the correct User object
        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void testDeleteUserNotFound() {
        // Mock the repository's findById method to return an empty optional
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        // Verify that the UserNotFoundException is thrown when deleting a non-existent User
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser("1"));

        // Verify that the repository's findById method was called with the correct ID
        verify(userRepository, times(1)).findById("1");

        // Verify that the repository's delete method was never called
        verify(userRepository, never()).delete(any(User.class));
    }




    @Test
    void testGetUsersByFirstName() {
        // Create a list of mock User objects with the same firstName
        String firstName = "FirstName";
        User mockUser1 = new User();
        mockUser1.setId("1");
        mockUser1.setFirstName(firstName);
        mockUser1.setLastName("LastName 1");
        mockUser1.setEmail("Email 1");
        mockUser1.setRole("Role 1");

        User mockUser2 = new User();
        mockUser2.setId("2");
        mockUser2.setFirstName(firstName);
        mockUser2.setLastName("LastName 2");
        mockUser2.setEmail("Email 2");
        mockUser2.setRole("Role 2");

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);

        // Mock the repository's findAllByFirstNameEqualsIgnoreCase method to return the mock Users
        when(userRepository.findAllByFirstNameEqualsIgnoreCase(firstName)).thenReturn(mockUsers);

        // Call the service method to get Users by firstName
        List<UserResponseDTO> result = userService.getUsersByFirstName(firstName);

        // Verify that the repository's findAllByFirstNameEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByFirstNameEqualsIgnoreCase(firstName);

        // Verify that the result contains the correct number of UserResponseDTO objects
        assertEquals(mockUsers.size(), result.size());

        // Verify that each UserResponseDTO in the result matches the corresponding mock User
        for (int i = 0; i < mockUsers.size(); i++) {
            User mockUser = mockUsers.get(i);
            UserResponseDTO responseDTO = result.get(i);
            assertEquals(mockUser.getId(), responseDTO.getId());
            assertEquals(mockUser.getFirstName(), responseDTO.getFirstName());
            assertEquals(mockUser.getLastName(), responseDTO.getLastName());
            assertEquals(mockUser.getEmail(), responseDTO.getEmail());
            assertEquals(mockUser.getRole(), responseDTO.getRole());

        }
    }

    @Test
    void testGetUsersByFirstNameNotFound() {
        // Mock the repository's findAllByFirstNameEqualsIgnoreCase method to return an empty list
        when(userRepository.findAllByFirstNameEqualsIgnoreCase("NonExistentFirstName")).thenReturn(List.of());

        // Verify that the UserNotFoundException is thrown when no Users are found
        assertThrows(UserNotFoundException.class, () -> userService.getUsersByFirstName("NonExistentFirstName"));

        // Verify that the repository's findAllByFirstNameEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByFirstNameEqualsIgnoreCase("NonExistentFirstName");
    }

    @Test
    void testGetUsersByLastName() {
        // Create a list of mock User objects with the same lastName
        String lastName = "LastName";
        User mockUser1 = new User();
        mockUser1.setId("1");
        mockUser1.setFirstName("FirstName 1");
        mockUser1.setLastName(lastName);
        mockUser1.setEmail("Email 1");
        mockUser1.setRole("Role 1");

        User mockUser2 = new User();
        mockUser2.setId("2");
        mockUser2.setFirstName("FirstName 2");
        mockUser2.setLastName(lastName);
        mockUser2.setEmail("Email 2");
        mockUser2.setRole("Role 2");

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);

        // Mock the repository's findAllByLastNameEqualsIgnoreCase method to return the mock Users
        when(userRepository.findAllByLastNameEqualsIgnoreCase(lastName)).thenReturn(mockUsers);

        // Call the service method to get Users by lastName
        List<UserResponseDTO> result = userService.getUsersByLastName(lastName);

        // Verify that the repository's findAllByLastNameEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByLastNameEqualsIgnoreCase(lastName);

        // Verify that the result contains the correct number of UserResponseDTO objects
        assertEquals(mockUsers.size(), result.size());

        // Verify that each UserResponseDTO in the result matches the corresponding mock User
        for (int i = 0; i < mockUsers.size(); i++) {
            User mockUser = mockUsers.get(i);
            UserResponseDTO responseDTO = result.get(i);
            assertEquals(mockUser.getId(), responseDTO.getId());
            assertEquals(mockUser.getFirstName(), responseDTO.getFirstName());
            assertEquals(mockUser.getLastName(), responseDTO.getLastName());
            assertEquals(mockUser.getEmail(), responseDTO.getEmail());
            assertEquals(mockUser.getRole(), responseDTO.getRole());
        }
    }

    @Test
    void testGetUsersByLastNameNotFound() {
        // Mock the repository's findAllByLastNameEqualsIgnoreCase method to return an empty list
        when(userRepository.findAllByLastNameEqualsIgnoreCase("NonExistentLastName")).thenReturn(List.of());

        // Verify that the UserNotFoundException is thrown when no Users are found
        assertThrows(UserNotFoundException.class, () -> userService.getUsersByLastName("NonExistentLastName"));

        // Verify that the repository's findAllByLastNameEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByLastNameEqualsIgnoreCase("NonExistentLastName");
    }

    @Test
    void testGetUsersByEmail() {
        // Create a list of mock User objects with the same email value
        String email = "Email Value";
        User mockUser1 = new User();
        mockUser1.setId("1");
        mockUser1.setFirstName("FirstName 1");
        mockUser1.setLastName("LastName 1");
        mockUser1.setEmail(email);
        mockUser1.setRole("Role 1");

        User mockUser2 = new User();
        mockUser2.setId("2");
        mockUser2.setFirstName("FirstName 2");
        mockUser2.setLastName("LastName 2");
        mockUser2.setEmail(email);
        mockUser2.setRole("Role 2");

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);

        // Mock the repository's findAllByEmailEqualsIgnoreCase method to return the mock Users
        when(userRepository.findAllByEmailEqualsIgnoreCase(email)).thenReturn(mockUsers);

        // Call the service method to get Users by email
        List<UserResponseDTO> result = userService.getUsersByEmail(email);

        // Verify that the repository's findAllByEmailEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByEmailEqualsIgnoreCase(email);

        // Verify that the result contains the correct number of UserResponseDTO objects
        assertEquals(mockUsers.size(), result.size());

        // Verify that each UserResponseDTO in the result matches the corresponding mock User
        for (int i = 0; i < mockUsers.size(); i++) {
            User mockUser = mockUsers.get(i);
            UserResponseDTO responseDTO = result.get(i);
            assertEquals(mockUser.getId(), responseDTO.getId());
            assertEquals(mockUser.getFirstName(), responseDTO.getFirstName());
            assertEquals(mockUser.getLastName(), responseDTO.getLastName());
            assertEquals(mockUser.getEmail(), responseDTO.getEmail());
            assertEquals(mockUser.getRole(), responseDTO.getRole());

        }
    }

    @Test
    void testGetUsersByEmailNotFound() {
        // Mock the repository's findAllByEmailEqualsIgnoreCase method to return an empty list
        when(userRepository.findAllByEmailEqualsIgnoreCase("NonExistentEmail")).thenReturn(List.of());

        // Verify that the UserNotFoundException is thrown when no Users are found
        assertThrows(UserNotFoundException.class, () -> userService.getUsersByEmail("NonExistentEmail"));

        // Verify that the repository's findAllByEmailEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByEmailEqualsIgnoreCase("NonExistentEmail");
    }

    @Test
    void testGetUsersByRole() {
        // Create a list of mock User objects with the same role value
        String role = "Role Value";
        User mockUser1 = new User();
        mockUser1.setId("1");
        mockUser1.setFirstName("FirstName 1");
        mockUser1.setLastName("LastName 1");
        mockUser1.setEmail("Email 1");
        mockUser1.setRole(role);

        User mockUser2 = new User();
        mockUser2.setId("2");
        mockUser2.setFirstName("FirstName 2");
        mockUser2.setLastName("LastName 2");
        mockUser2.setEmail("Email 2");
        mockUser2.setRole(role);

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2);

        // Mock the repository's findAllByRoleEqualsIgnoreCase method to return the mock Users
        when(userRepository.findAllByRoleEqualsIgnoreCase(role)).thenReturn(mockUsers);

        // Call the service method to get Users by role
        List<UserResponseDTO> result = userService.getUsersByRole(role);

        // Verify that the repository's findAllByRoleEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByRoleEqualsIgnoreCase(role);

        // Verify that the result contains the correct number of UserResponseDTO objects
        assertEquals(mockUsers.size(), result.size());

        // Verify that each UserResponseDTO in the result matches the corresponding mock User
        for (int i = 0; i < mockUsers.size(); i++) {
            User mockUser = mockUsers.get(i);
            UserResponseDTO responseDTO = result.get(i);
            assertEquals(mockUser.getId(), responseDTO.getId());
            assertEquals(mockUser.getFirstName(), responseDTO.getFirstName());
            assertEquals(mockUser.getLastName(), responseDTO.getLastName());
            assertEquals(mockUser.getEmail(), responseDTO.getEmail());
            assertEquals(mockUser.getRole(), responseDTO.getRole());
        }
    }

    @Test
    void testGetUsersByRoleNotFound() {
        // Mock the repository's findAllByRoleEqualsIgnoreCase method to return an empty list
        when(userRepository.findAllByRoleEqualsIgnoreCase("NonExistentRole")).thenReturn(List.of());

        // Verify that the UserNotFoundException is thrown when no Users are found
        assertThrows(UserNotFoundException.class, () -> userService.getUsersByRole("NonExistentRole"));

        // Verify that the repository's findAllByRoleEqualsIgnoreCase method was called with the correct parameter
        verify(userRepository, times(1)).findAllByRoleEqualsIgnoreCase("NonExistentRole");
    }


}