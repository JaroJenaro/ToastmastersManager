package de.iav.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpeechContributionTest {

    private SpeechContribution speechContribution;

    @BeforeEach
    void setUp() {
        // Erstellen von Beispieldaten für die Tests
        speechContribution = new SpeechContribution();
        speechContribution.setId("1");
        TimeSlot timeSlot = new TimeSlot();
        timeSlot.setId("101");
        User user = new User();
        user.setId("201");
        speechContribution.setTimeSlot(timeSlot);
        speechContribution.setUser(user);
        speechContribution.setStoppedTime("2023-09-29 10:30 AM");
    }
    @Test
    void testGetId() {
        assertEquals("1", speechContribution.getId());
    }

    @Test
    void testGetTimeSlot() {
        assertEquals("101", speechContribution.getTimeSlot().getId());
    }

    @Test
    void testGetUser() {
        assertEquals("201", speechContribution.getUser().getId());
    }

    @Test
    void testGetStoppedTime() {
        assertEquals("2023-09-29 10:30 AM", speechContribution.getStoppedTime());
    }

    @Test
    void testSetId() {
        speechContribution.setId("2");
        assertEquals("2", speechContribution.getId());
    }

    @Test
    void testSetTimeSlot() {
        TimeSlot newTimeSlot = new TimeSlot();
        newTimeSlot.setId("102");
        speechContribution.setTimeSlot(newTimeSlot);
        assertEquals("102", speechContribution.getTimeSlot().getId());
    }

    @Test
    void testSetUser() {
        User newUser = new User();
        newUser.setId("202");
        speechContribution.setUser(newUser);
        assertEquals("202", speechContribution.getUser().getId());
    }

    @Test
    void testSetStoppedTime() {
        speechContribution.setStoppedTime("2023-09-30 11:00 AM");
        assertEquals("2023-09-30 11:00 AM", speechContribution.getStoppedTime());
    }
}