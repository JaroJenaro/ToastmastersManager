package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeSlotWithoutIdDTO {
    String title;
    String description;
    String green;
    String amber;
    String red;

    public TimeSlot getTimeSlotWithoutId(){
        return new TimeSlot( this.title, this.description, this.green, this.amber, this.red);
    }
}
