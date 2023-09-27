package de.iav.backend.controller;

import de.iav.backend.model.TimeSlotDTO;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import de.iav.backend.model.TimeSlot;
import de.iav.backend.service.TimeSlotService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/toastMasterManager/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;


    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;

    }

    @GetMapping
    public List<TimeSlot> getAllStocks() {
        return timeSlotService.getAllTimeSlots();
    }

    @GetMapping("/{id}")
    public Optional<TimeSlot> getStockById(@PathVariable String id) {
        return timeSlotService.getTimeSlotById(id);
    }

    @GetMapping("/set")
    public List<TimeSlot> setDefaultTimeSlots() {
        return timeSlotService.setTimeSlotRepository();
    }

    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(@RequestBody TimeSlotWithoutIdDTO timeSlotWithoutIdDTO) {
        TimeSlot createdTimeSlot = timeSlotService.saveTimeSlot(timeSlotWithoutIdDTO.getTimeSlotWithoutId());
        return ResponseEntity.created(URI.create("/timeslots/" + createdTimeSlot.getId())).body(createdTimeSlot);
    }

    @PutMapping
    public ResponseEntity<TimeSlot> updateTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO) {
        TimeSlot updatedTimeSlot = timeSlotService.saveTimeSlot(timeSlotDTO.getTimeSlot());
        return ResponseEntity.created(URI.create("/timeslots/" + updatedTimeSlot.getId())).body(updatedTimeSlot);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimeSlot(@PathVariable String id) {
        timeSlotService.deleteTimeSlot(id);
    }
}

