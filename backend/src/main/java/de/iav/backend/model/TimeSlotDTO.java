package de.iav.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TimeSlotDTO {
    String id;
    String title;
    String description;
    String green;
    String amber;
    String red;

    public TimeSlot getTimeSlot(){
        return new TimeSlot(this.id, this.title, this.description, this.green, this.amber, this.red);
    }
}
