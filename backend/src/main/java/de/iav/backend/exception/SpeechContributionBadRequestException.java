package de.iav.backend.exception;

public class SpeechContributionBadRequestException extends RuntimeException {
    public SpeechContributionBadRequestException(String info) {
        super("Unerlaubte SpeechContribution  Operation: " + info + " ist aufgetreten");
    }
}