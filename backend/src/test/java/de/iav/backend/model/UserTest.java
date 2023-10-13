package de.iav.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        // Erstellen einer Beispiel-User-Instanz für die Tests
        user = new User();
        user.setId("1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setRole("USER");
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.iterator().next().getAuthority());
    }

    @Test
    void testGetUsername() {
        assertEquals("john.doe@example.com", user.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testGetId() {
        assertEquals("1", user.getId());
    }

    @Test
    void testGetFirstName() {
        assertEquals("John", user.getFirstName());
    }

    @Test
    void testGetLastName() {
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testGetPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    void testGetRole() {
        assertEquals("USER", user.getRole());
    }

    @Test
    void testSetId() {
        User user = new User();
        user.setId("2");
        assertEquals("2", user.getId());
    }

    @Test
    void testSetFirstName() {
        User user = new User();
        user.setFirstName("Alice");
        assertEquals("Alice", user.getFirstName());
    }

    @Test
    void testSetLastName() {
        User user = new User();
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void testSetEmail() {
        User user = new User();
        user.setEmail("alice.smith@example.com");
        assertEquals("alice.smith@example.com", user.getEmail());
    }

    @Test
    void testSetPassword() {
        User user = new User();
        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());
    }

    @Test
    void testSetRole() {
        User user = new User();
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
     void testUserBuilder() {
        User user = User.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role("USER")
                .build();

        assertEquals("1", user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = User.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role("USER")
                .build();

        User user2 = User.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role("USER")
                .build();

        User user3 = User.builder()
                .id("2")
                .firstName("Alice")
                .lastName("Smith")
                .email("alice.smith@example.com")
                .password("password")
                .role("ADMIN")
                .build();

        // Testen der Gleichheit (equals und hashCode sollten korrekt implementiert sein)
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertNotEquals(user2, user3);

        // Testen der hashCode-Konsistenz
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role("USER")
                .build();

        String expectedToString = "User(id=1, firstName=John, lastName=Doe, email=john.doe@example.com, password=password, role=USER)";

        assertEquals(expectedToString, user.toString());
    }
}