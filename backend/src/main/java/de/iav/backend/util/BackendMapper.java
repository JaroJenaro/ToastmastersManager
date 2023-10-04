package de.iav.backend.util;


import de.iav.backend.model.TimeSlot;
import de.iav.backend.model.TimeSlotWithoutIdDTO;

public class BackendMapper {


    public TimeSlot mapToTimeSlotWithoutIdDTO(TimeSlotWithoutIdDTO timeSlotWithoutIdDTO){
        return new TimeSlot(null,
                timeSlotWithoutIdDTO.getTitle(),
                timeSlotWithoutIdDTO.getDescription(),
                timeSlotWithoutIdDTO.getGreen(),
                timeSlotWithoutIdDTO.getAmber(),
                timeSlotWithoutIdDTO.getRed());
    }

}
