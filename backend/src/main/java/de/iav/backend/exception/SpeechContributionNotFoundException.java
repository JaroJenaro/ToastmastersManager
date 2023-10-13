package de.iav.backend.exception;

public class SpeechContributionNotFoundException extends RuntimeException {
    public SpeechContributionNotFoundException(String id) {
        super("SpeechContribution with id " + id + " not found");
    }
}