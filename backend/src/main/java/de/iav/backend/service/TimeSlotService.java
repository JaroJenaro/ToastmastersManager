package de.iav.backend.service;

import de.iav.backend.model.TimeSlot;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.iav.backend.repository.TimeSlotRepository;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TimeSlotService {

    public final List<TimeSlotWithoutIdDTO> tempTimeSlots = new ArrayList<>(Arrays.asList(
            new TimeSlotWithoutIdDTO( "1 Speaker", "Erste vorbereitete Rede", "5:00", "6:00", "7:00"),
            new TimeSlotWithoutIdDTO( "2 Speaker", "Zweite vorbereitete Rede", "5:00", "6:00", "7:00"),
            new TimeSlotWithoutIdDTO( "3 Speaker", "Dritte vorbereitete Rede", "5:00", "6:00", "7:00"),
            new TimeSlotWithoutIdDTO( "Table Topic Master", "Moderator der Stegreifreden", "1:30", "2:00", "2:30"),
            new TimeSlotWithoutIdDTO( "1 Table Topic", "Dritte vorbereitete Rede", "1:00", "1:30", "2:00"))
    );


    public final TimeSlotRepository timeSlotRepository;


    public List<TimeSlot> getAllTimeSlots(){
        return timeSlotRepository.findAll();
    }


    public Optional<TimeSlot> getTimeSlotById(String id){
        return timeSlotRepository.findById(id);
    }


    public List<TimeSlot> setTimeSlotRepository(){
        if (timeSlotRepository.findAll().isEmpty())
            return fillDataWithTimeSlots();
        else
            return timeSlotRepository.findAll();

    }


    private List<TimeSlot> fillDataWithTimeSlots(){
        List<TimeSlot> savedTimeSlots = new ArrayList<>();
        for (TimeSlot timeSlot:getTimeSlotsWithoutId(tempTimeSlots)
             ) {
            savedTimeSlots.add(timeSlotRepository.save(timeSlot));

        }
        return savedTimeSlots;
    }


    private List<TimeSlot> getTimeSlotsWithoutId(List<TimeSlotWithoutIdDTO> timeSlotWithoutIdDTOList){
        List<TimeSlot> timeSlotLists = new ArrayList<>();
        for (TimeSlotWithoutIdDTO timeSlotWithoutIdDTO: timeSlotWithoutIdDTOList
             ) {
            timeSlotLists.add(timeSlotWithoutIdDTO.getTimeSlotWithoutId());

        }
        return timeSlotLists;
    }

    public TimeSlot saveTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    public void deleteTimeSlot(String id) {
        timeSlotRepository.deleteById(id);
    }
}