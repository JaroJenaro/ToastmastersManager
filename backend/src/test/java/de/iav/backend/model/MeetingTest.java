package de.iav.backend.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Test
    void testEquals() {
        Meeting meeting1 = new Meeting("1", "2023-09-29 10:00 AM", "Meeting Room A", null);
        Meeting meeting2 = new Meeting("2", "2023-09-30 2:00 PM", "Meeting Room B", null);

        // Meetings mit unterschiedlichen IDs sollten nicht gleich sein
        assertNotEquals(meeting1, meeting2);

        Meeting meeting3 = new Meeting("1", "2023-09-29 10:00 AM", "Meeting Room A", null);

        // Meetings mit denselben IDs sollten gleich sein
        assertEquals(meeting1, meeting3);
    }

    @Test
    void testToString() {
        Meeting meeting = new Meeting("1", "2023-09-29 10:00 AM", "Meeting Room A", null);
        String expectedToString = "Meeting(id=1, dateTime=2023-09-29 10:00 AM, location=Meeting Room A, speechContributionList=null)";
        assertEquals(expectedToString, meeting.toString());
    }

    @Test
    void testHashCode() {
        Meeting meeting1 = new Meeting("1", "2023-09-29 10:00 AM", "Meeting Room A", null);
        Meeting meeting2 = new Meeting("2", "2023-09-30 2:00 PM", "Meeting Room B", null);

        // Hash-Codes von Meetings mit unterschiedlichen IDs sollten unterschiedlich sein
        assertNotEquals(meeting1.hashCode(), meeting2.hashCode());

        Meeting meeting3 = new Meeting("1", "2023-09-29 10:00 AM", "Meeting Room A", null);

        // Hash-Codes von Meetings mit denselben IDs sollten gleich sein
        assertEquals(meeting1.hashCode(), meeting3.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        Meeting meeting = new Meeting();

        meeting.setId("1");
        assertEquals("1", meeting.getId());

        meeting.setDateTime("2023-09-29 10:00 AM");
        assertEquals("2023-09-29 10:00 AM", meeting.getDateTime());

        meeting.setLocation("Meeting Room A");
        assertEquals("Meeting Room A", meeting.getLocation());

        List<SpeechContribution> speechContributions = Arrays.asList(new SpeechContribution(), new SpeechContribution());
        meeting.setSpeechContributionList(speechContributions);
        assertThat(meeting.getSpeechContributionList()).isEqualTo(speechContributions);
    }
}