package de.iav.backend.service;

import de.iav.backend.exception.SpeechContributionBadRequestException;
import de.iav.backend.exception.SpeechContributionNotFoundException;
import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.exception.UserNotFoundException;
import de.iav.backend.model.*;
import de.iav.backend.repository.SpeechContributionRepository;
import de.iav.backend.repository.TimeSlotRepository;
import de.iav.backend.repository.UserRepository;
import de.iav.backend.util.BackendBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeechContributionService {

    private final SpeechContributionRepository speechContributionRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final UserRepository userRepository;

    public List<SpeechContributionDTO> getAllSpeechContributions(){
        return speechContributionRepository.findAll()
                .stream()
                .map(BackendBuilder::getSpeechContributionDTO)
                .toList();
    }

    public SpeechContributionDTO getSpeechContributionById(String id){
        SpeechContribution speechContribution = speechContributionRepository.
                findById(id).
                orElseThrow(() -> new SpeechContributionNotFoundException(id));

        return BackendBuilder.getSpeechContributionDTO(speechContribution);
    }

    public SpeechContributionDTO addSpeechContribution(SpeechContributionIn speechContributionIn) {
        SpeechContribution speechContribution = BackendBuilder.getSpeechContribution(speechContributionIn);

        SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContribution);

        return BackendBuilder.getSpeechContributionDTO(savedSpeechContribution);
    }

    public List<SpeechContributionDTO> addSpeechContributionList(List<SpeechContributionDTO> speechContributionDtoToSaveList) {
        List<SpeechContributionDTO> speechContributionDTOList = new ArrayList<>();
        for (SpeechContributionDTO getSpeechContributionFromDTO:speechContributionDtoToSaveList
             ) {
            SpeechContribution speechContribution =  BackendBuilder.getSpeechContributionFromDTO(getSpeechContributionFromDTO);

            SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContribution);

            speechContributionDTOList.add(BackendBuilder.getSpeechContributionDTO(savedSpeechContribution));
        }
        return speechContributionDTOList;
    }


    public SpeechContributionDTO updateSpeechContribution(SpeechContributionIn speechContributionIn, String id) {
        SpeechContribution speechContributionToUpdateFromDB = speechContributionRepository
                .findById(id)
                .orElseThrow(() -> new SpeechContributionNotFoundException(id));
        // Timeslot darf nicht geändert werden oder getauscht werden
        TimeSlot timeSlotFromDB = timeSlotRepository
                .findById(speechContributionToUpdateFromDB.getTimeSlot().getId())
                .orElseThrow(() -> new TimeSlotNotFoundException(speechContributionIn.getTimeSlot().getId()));
        // User darf getauscht oder entfernt werden
        TimeSlot timeSlotIn = getTimeSlot(speechContributionIn.getTimeSlot());
        User userInFromDB;
        if(speechContributionIn.getUser() != null) {
            userInFromDB = userRepository
                    .findById(speechContributionIn.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException(speechContributionIn.getUser().getId()));

            User userIn = BackendBuilder.getUserFromResponse(speechContributionIn.getUser());
            userIn.setPassword(userInFromDB.getPassword());
            if(timeSlotFromDB.equals(timeSlotIn) && userInFromDB.equals(userIn))
            {
                speechContributionToUpdateFromDB.setUser(BackendBuilder.getUserFromResponse(speechContributionIn.getUser()));
                speechContributionToUpdateFromDB.setTimeSlot(getTimeSlot(speechContributionIn.getTimeSlot()));
                speechContributionToUpdateFromDB.setStoppedTime(speechContributionIn.getStoppedTime());

                SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContributionToUpdateFromDB);

                return BackendBuilder.getSpeechContributionDTO(savedSpeechContribution);
            }
        }
        else if(timeSlotFromDB.equals(timeSlotIn))
        {
            speechContributionToUpdateFromDB.setUser(null);
            speechContributionToUpdateFromDB.setTimeSlot(getTimeSlot(speechContributionIn.getTimeSlot()));
            speechContributionToUpdateFromDB.setStoppedTime(speechContributionIn.getStoppedTime());

            SpeechContribution savedSpeechContribution = speechContributionRepository.save(speechContributionToUpdateFromDB);

            return BackendBuilder.getSpeechContributionDTO(savedSpeechContribution);
        }
        throw new SpeechContributionBadRequestException(" beim updateSpeechContribution ");
    }

    public void deleteSpeechContribution(String id) {
        SpeechContribution speechContributionToDELETE = speechContributionRepository
                .findById(id)
                .orElseThrow(() -> new SpeechContributionNotFoundException(id));
        speechContributionRepository.delete(speechContributionToDELETE);
    }

    private TimeSlot getTimeSlot(TimeSlotResponseDTO timeSlotResponseDTO){
        return TimeSlot.builder()
                .id(timeSlotResponseDTO.getId())
                .title(timeSlotResponseDTO.getTitle())
                .description(timeSlotResponseDTO.getDescription())
                .green(timeSlotResponseDTO.getGreen())
                .amber(timeSlotResponseDTO.getAmber())
                .red(timeSlotResponseDTO.getRed())
                .build();
    }

}