package de.iav.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;

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

}

