package de.iav.backend.controller;

import de.iav.backend.model.MeetingRequestDTO;
import de.iav.backend.model.MeetingResponseDTO;
import de.iav.backend.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/toast-master-manager/meetings")
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping
    public List<MeetingResponseDTO> getAllMeetings() {
        return meetingService.getAllSMeetings();
    }

    @GetMapping("/{id}")
    public MeetingResponseDTO getMeetingById(@PathVariable String id) {
        return meetingService.getMeetingById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeetingResponseDTO createMeeting(@Valid @RequestBody MeetingRequestDTO meetingRequestDto) {
        return meetingService.addMeeting(meetingRequestDto);
    }

    @GetMapping("/search")
    public MeetingResponseDTO searchMeetingByStandortAndDateTime(
            @RequestParam String dateTime,
            @RequestParam String location

    ) {
        return meetingService.getMeetingByDateTimeAndLocation(dateTime, location);
    }

    @PutMapping("/{id}")
    public MeetingResponseDTO createMeeting(@PathVariable String id,@Valid @RequestBody MeetingRequestDTO meetingRequestDto) {
        return meetingService.updateMeeting(id, meetingRequestDto);
    }
}