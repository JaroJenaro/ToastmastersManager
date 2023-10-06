package de.iav.backend.controller;

import de.iav.backend.model.TimeSlotResponseDTO;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import de.iav.backend.model.TimeSlot;
import de.iav.backend.service.TimeSlotService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/toastMasterManager/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;


    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @GetMapping
    public List<TimeSlotResponseDTO> getAllTimeSlots(            @RequestParam(required = false) String title,
                                                                 @RequestParam(required = false) String description,
                                                                 @RequestParam(required = false) String green,
                                                                 @RequestParam(required = false) String amber,
                                                                 @RequestParam(required = false) String red) {

        if (title != null) {
            return timeSlotService.getTimeSlotsByTitle(title);
        } else if (description != null) {
            return timeSlotService.getTimeSlotsByDescription(description);
        } else if (green != null) {
            return timeSlotService.getTimeSlotsByGreen(green);
        }
        else if (amber != null) {
            return timeSlotService.getTimeSlotsByAmber(amber);
        }
        else if (red != null) {
            return timeSlotService.getTimeSlotsByRed(red);
        }

        return timeSlotService.getAllTimeSlots();
    }





    @GetMapping("/{id}")
    public TimeSlotResponseDTO getTimeSlotById(@PathVariable String id) {
        return timeSlotService.getTimeSlotById(id);
    }

    @GetMapping("/search")
    public TimeSlotResponseDTO searchTimeSlotByTitleAndDescription(
            @RequestParam String title,
            @RequestParam String description
    ) {
           return timeSlotService.getTimeSlotByTitleAndDescription(title, description);
    }

    @GetMapping("/search2")
    public TimeSlotResponseDTO searchTimeSlotByTitleAndRed(
            @RequestParam String title,
            @RequestParam String red
    ) {
        return timeSlotService.getTimeSlotByTitleAndRed(title, red);
    }

    @GetMapping("/set")
    public List<TimeSlot> setDefaultTimeSlots() {
        return timeSlotService.setTimeSlotRepository();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TimeSlotResponseDTO createTimeSlot(@Valid @RequestBody TimeSlotWithoutIdDTO timeSlotWithoutIdDTO) {
        return timeSlotService.addTimeSlot(timeSlotWithoutIdDTO);

    }

    @PutMapping("/{id}")
    public TimeSlotResponseDTO updateTimeSlot(@PathVariable String id, @Valid @RequestBody TimeSlotWithoutIdDTO timeSlotWithoutIdDTO) {
        return timeSlotService.updateTimeSlot(timeSlotWithoutIdDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimeSlot(@PathVariable String id) {
        timeSlotService.deleteTimeSlot(id);
    }

}

