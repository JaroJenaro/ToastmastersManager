package de.iav.frontend.model;

public record TimeSlot(
    String id,
    String title,
    String description,
    String green,
    String amber,
    String red


) {
        public String toString() {
            return title + ": " + description + ": " + red;
        }
    }
