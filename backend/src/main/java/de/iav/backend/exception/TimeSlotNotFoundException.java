package de.iav.backend.exception;

public class TimeSlotNotFoundException extends RuntimeException{
    public TimeSlotNotFoundException(String id) {
        super("TimeSlot with id " + id + " not found");
    }
}