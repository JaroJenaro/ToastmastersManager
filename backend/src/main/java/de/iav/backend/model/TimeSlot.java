package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document(collection = "timeslots")
public class TimeSlot {
    @MongoId
    String id;
    String title;
    String description;
    String green;
    String amber;
    String red;
    public TimeSlot(String title, String description, String green, String amber, String red) {
        this.title = title;
        this.description = description;
        this.green = green;
        this.amber = amber;
        this.red = red;

    }
}

