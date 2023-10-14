package de.iav.backend.exception;

public class MeetingNotFoundException extends RuntimeException{
    public MeetingNotFoundException(String id) {
        super("Meeting with id " + id + " not found");
    }
}