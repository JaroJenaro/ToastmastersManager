package de.iav.frontend.model;

public record SpeechContribution(
        String id,
        TimeSlot timeSlot,
        User user,
        String stoppedTime) {
}
