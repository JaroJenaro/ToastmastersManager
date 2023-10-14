package de.iav.backend.service;

import de.iav.backend.exception.*;
import de.iav.backend.model.*;
import de.iav.backend.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    public List<MeetingResponseDTO> getAllSMeetings(){
        return meetingRepository.findAll()
                .stream()
                .map(this::getMeetingResponseDTO)
                .toList();
    }

    public MeetingResponseDTO getMeetingById(String id){
        Meeting meeting = meetingRepository.
                findById(id).
                orElseThrow(() -> new MeetingNotFoundException(id));

        return getMeetingResponseDTO(meeting);
    }

    private MeetingResponseDTO getMeetingResponseDTO(Meeting meeting){
        return MeetingResponseDTO.builder()
                .id(meeting.getId())
                .dateTime(meeting.getDateTime())
                .location(meeting.getLocation())
                .speechContributionList(meeting.getSpeechContributionList()
                        .stream()
                        .map(speechContribution -> SpeechContributionDTO.builder()
                                .id(speechContribution.getId())
                                .user(UserResponseDTO.builder()
                                        .id(speechContribution.getUser().getId())
                                        .firstName(speechContribution.getUser().getFirstName())
                                        .lastName(speechContribution.getUser().getLastName())
                                        .email(speechContribution.getUser().getEmail())
                                        .role(speechContribution.getUser().getRole())
                                        .build())
                                .timeSlot(TimeSlotResponseDTO.builder()
                                        .id(speechContribution.getTimeSlot().getId())
                                        .title(speechContribution.getTimeSlot().getTitle())
                                        .description(speechContribution.getTimeSlot().getDescription())
                                        .green(speechContribution.getTimeSlot().getGreen())
                                        .amber(speechContribution.getTimeSlot().getAmber())
                                        .red(speechContribution.getTimeSlot().getRed())
                                        .build())
                                .stoppedTime(speechContribution.getStoppedTime())
                                .build())
                        .toList())
                .build();
    }

    private Meeting getMeeting(MeetingRequestDTO meetingRequestDto){
        return Meeting.builder()
                .dateTime(meetingRequestDto.getDateTime())
                .location(meetingRequestDto.getLocation())
                .speechContributionList(meetingRequestDto.getSpeechContributionList()
                        .stream()
                        .map(speechContribution -> SpeechContribution.builder()
                                .id(speechContribution.getId())
                                .user(User.builder()
                                        .id(speechContribution.getUser().getId())
                                        .firstName(speechContribution.getUser().getFirstName())
                                        .lastName(speechContribution.getUser().getLastName())
                                        .email(speechContribution.getUser().getEmail())
                                        .role(speechContribution.getUser().getRole())
                                        .build())
                                .timeSlot(TimeSlot.builder()
                                        .id(speechContribution.getTimeSlot().getId())
                                        .title(speechContribution.getTimeSlot().getTitle())
                                        .description(speechContribution.getTimeSlot().getDescription())
                                        .green(speechContribution.getTimeSlot().getGreen())
                                        .amber(speechContribution.getTimeSlot().getAmber())
                                        .red(speechContribution.getTimeSlot().getRed())
                                        .build())
                                .stoppedTime(speechContribution.getStoppedTime())
                                .build())
                        .toList())
                .build();
    }

    public MeetingResponseDTO addMeeting(MeetingRequestDTO meetingRequestDto) {

        Meeting meeting = getMeeting(meetingRequestDto);

        Meeting savedMeeting = meetingRepository.save(meeting);

        return getMeetingResponseDTO(savedMeeting);
    }
}