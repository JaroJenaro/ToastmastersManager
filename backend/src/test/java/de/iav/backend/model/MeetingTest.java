package de.iav.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTest {
    private Meeting meeting;
    @BeforeEach
    public void setUp() {
        // Erstellen einer Beispiel-Meeting-Instanz für jeden Testfall
        meeting = new Meeting();
        meeting.setId("1");
        meeting.setDateTime("2023-09-29 10:00 AM");
        meeting.setLocation("Meeting Room A");
        List<SpeechContribution> speechContributions = new ArrayList<>();
        SpeechContribution contribution1 = new SpeechContribution();
        contribution1.setId("1");
        SpeechContribution contribution2 = new SpeechContribution();
        contribution2.setId("2");
        speechContributions.add(contribution1);
        speechContributions.add(contribution2);
        meeting.setSpeechContributionList(speechContributions);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void getId() {
        assertEquals("1", meeting.getId());
    }

    @Test
    void getDateTime() {
        assertEquals("2023-09-29 10:00 AM", meeting.getDateTime());
    }

    @Test
    void getLocation() {
        assertEquals("Meeting Room A", meeting.getLocation());
    }

    @Test
    void getSpeechContributionList() {
        List<SpeechContribution> speechContributions = meeting.getSpeechContributionList();
        assertEquals(2, speechContributions.size());
        assertEquals("1", speechContributions.get(0).getId());
        assertEquals("2", speechContributions.get(1).getId());
    }

    @Test
    void setId() {
        Meeting meeting = new Meeting();
        meeting.setId("1");
        assertEquals("1", meeting.getId());
    }

    @Test
    void setDateTime() {
        Meeting meeting = new Meeting();
        meeting.setDateTime("2023-09-29 10:00 AM");
        assertEquals("2023-09-29 10:00 AM", meeting.getDateTime());
    }

    @Test
    void setLocation() {
        Meeting meeting = new Meeting();
        meeting.setLocation("Meeting Room A");
        assertEquals("Meeting Room A", meeting.getLocation());
    }

    @Test
    void setSpeechContributionList() {
        Meeting meeting = new Meeting();
        List<SpeechContribution> speechContributions = new ArrayList<>();
        SpeechContribution contribution1 = new SpeechContribution();
        contribution1.setId("1");
        SpeechContribution contribution2 = new SpeechContribution();
        contribution2.setId("2");
        speechContributions.add(contribution1);
        speechContributions.add(contribution2);

        meeting.setSpeechContributionList(speechContributions);

        List<SpeechContribution> retrievedSpeechContributions = meeting.getSpeechContributionList();
        assertEquals(2, retrievedSpeechContributions.size());
        assertEquals("1", retrievedSpeechContributions.get(0).getId());
        assertEquals("2", retrievedSpeechContributions.get(1).getId());
    }

    @Test
    void testMeetingBuilder() {
        Meeting meeting2 = Meeting.builder()
                .id("3")
                .dateTime("2023-09-30 2:00 PM")
                .location("Meeting Room B")
                .build();
        // Überprüfen, ob die Werte korrekt gesetzt wurden
        assertEquals("3", meeting2.getId());
        assertEquals("2023-09-30 2:00 PM", meeting2.getDateTime());
        assertEquals("Meeting Room B", meeting2.getLocation());
    }
}