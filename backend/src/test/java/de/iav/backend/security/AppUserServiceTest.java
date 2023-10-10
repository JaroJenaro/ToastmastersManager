package de.iav.backend.security;

import de.iav.backend.model.User;
import de.iav.backend.model.UserResponseDTO;
import de.iav.backend.repository.UserRepository;
import de.iav.backend.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


import static org.mockito.Mockito.*;

import java.util.Optional;

@SpringBootTest
class AppUserServiceTest {


    private final UserRepository userRepository = mock(UserRepository.class);
    private final AppUserService appUserService = new AppUserService(userRepository);
    @Test
    void testLoadUserByUsername_UserFound() {
        // Mock the UserRepository to return a user when findByEmail is called
        String userEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(userEmail);
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.of(mockUser));

        // Call the loadUserByUsername method
        UserDetails userDetails = appUserService.loadUserByUsername(userEmail);

        // Verify that the UserRepository's findUserByEmail method was called
        verify(userRepository, times(1)).findUserByEmail(userEmail);

        // Verify that the returned UserDetails matches the mockUser
        assertEquals(mockUser.getEmail(), userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock the UserRepository to return an empty Optional when findByEmail is called
        String userEmail = "nonexistent@example.com";
        when(userRepository.findUserByEmail(userEmail)).thenReturn(Optional.empty());

        // Call the loadUserByUsername method and expect a UsernameNotFoundException
        assertThrows(UsernameNotFoundException.class, () -> appUserService.loadUserByUsername(userEmail));

        // Verify that the UserRepository's findUserByEmail method was called
        verify(userRepository, times(1)).findUserByEmail(userEmail);
    }

    @Test
    void testRegister() {
        // Mocking des UserRepository und des PasswordEncoders
        UserRepository userRepository = Mockito.mock(UserRepository.class);
        Argon2PasswordEncoder passwordEncoder = Mockito.mock(Argon2PasswordEncoder.class);

        // Erstellen eines Testbenutzers
        NewAppUser newAppUser = new NewAppUser("testuser","John", "Doe", "john@example.com", "password");

        // Erstellen eines Mock-Benutzers für die Datenbank
        User savedUserInDatabase = User.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("encodedPassword")
                .role("USER")
                .build();

        // Stubben der Methoden für den UserRepository und den PasswordEncoder
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUserInDatabase);

        // Erstellen des UserService mit den gemockten Abhängigkeiten
        UserService userService = new UserService(userRepository);

        // Registrieren des Benutzers
        UserResponseDTO userResponseDTO = userService.register(newAppUser);

        // Überprüfen, ob die richtigen Methoden aufgerufen wurden
        //Mockito.verify(passwordEncoder).encode("password");
        //Mockito.verify(userRepository).save(Mockito.any(User.class));

        // Überprüfen, ob die zurückgegebene Benutzerantwort korrekt ist
        assertEquals("1", userResponseDTO.getId());
        assertEquals("John", userResponseDTO.getFirstName());
        assertEquals("Doe", userResponseDTO.getLastName());
        assertEquals("john@example.com", userResponseDTO.getEmail());
        assertEquals("USER", userResponseDTO.getRole());
    }
}