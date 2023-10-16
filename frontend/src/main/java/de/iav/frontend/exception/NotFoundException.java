package de.iav.frontend.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String notFoundObject) {
        super("Könnte nicht gefunden werden: " + notFoundObject);
    }
}