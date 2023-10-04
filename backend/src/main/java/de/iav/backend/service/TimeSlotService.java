package de.iav.backend.service;

import de.iav.backend.exception.TimeSlotNotFoundException;
import de.iav.backend.model.TimeSlot;
import de.iav.backend.model.TimeSlotResponseDTO;
import de.iav.backend.model.TimeSlotWithoutIdDTO;
import de.iav.backend.util.BackendMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import de.iav.backend.repository.TimeSlotRepository;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TimeSlotService {
    private static final String WAS_NOT_FOUND = " was not found.";


    public final List<TimeSlotWithoutIdDTO> tempTimeSlots = new ArrayList<>(Arrays.asList(
            new TimeSlotWithoutIdDTO( "Break", "Ankunft, Begrüßung der Geste", null, null, "15:00"),
            new TimeSlotWithoutIdDTO( "Toastmaster", "Toastmaster des Abends Begrüßung Vorstellung des Abends", "4:00", "5:00", "6:00"),
            new TimeSlotWithoutIdDTO( "Word of the Day", "Wort welches in allen Redebeiträgen benutzt werden soll", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "Joke of the Day", "Witz des Tages", null, null, "1:00"),
            new TimeSlotWithoutIdDTO( "1 Speaker", "Erste vorbereitete Rede", "5:00", "6:00", "7:00"),
            new TimeSlotWithoutIdDTO( "Bewertung Break1", "Bewertung der Rede 1", null, null, "1:00"),
            new TimeSlotWithoutIdDTO( "2 Speaker", "Zweite vorbereitete Rede", "5:00", "6:00", "7:00"),
            new TimeSlotWithoutIdDTO( "Bewertung Break2", "Bewertung der Rede 2", null, null, "1:00"),
            new TimeSlotWithoutIdDTO( "3 Speaker", "Dritte vorbereitete Rede", "5:00", "6:00", "7:00"),
            new TimeSlotWithoutIdDTO( "Bewertung Break3", "Bewertung der Rede 3", null, null, "1:00"),
            new TimeSlotWithoutIdDTO( "Table Topic Master", "Moderator der Stegreifreden", "1:30", "2:00", "2:30"),
            new TimeSlotWithoutIdDTO( "1st Table Topic Volunteer", "Erste Stegreif-Redner", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "2st Table Topic Volunteer", "Zweite Stegreif-Redner", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "3st Table Topic Volunteer", "Dritte Stegreif-Redner", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "4st Table Topic Volunteer", "Vierte Stegreif-Redner", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "5st Table Topic Volunteer", "Fünfte Stegreif-Redner", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "Bewertung Break Stegreifrede", "Bewertung der Stegreif-Redner", null, null, "1:00"),
            new TimeSlotWithoutIdDTO( "Break", "Pause", null, null, "5:00"),
            new TimeSlotWithoutIdDTO( "Toastmaster Bewertung", "Vorstellung der Bewertung", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "1st Evaluator", "Erste Rede Bewerter", "2:00", "2:30", "3:00"),
            new TimeSlotWithoutIdDTO( "2st Evaluator", "Zweite Rede Bewerter", "2:00", "2:30", "3:00"),
            new TimeSlotWithoutIdDTO( "3st Evaluator", "Dritte Rede Bewerter", "2:00", "2:30", "3:00"),
            new TimeSlotWithoutIdDTO( "Grammarian", "Bewertung der Grammatik", "2:00", "3:00", "4:00"),
            new TimeSlotWithoutIdDTO( "Ah Counter", "Beobachter von Füllwörtern", "1:00", "2:00", "3:00"),
            new TimeSlotWithoutIdDTO( "Timekeeper", "Bericht des Zeitnehmers", null, null, "3:00"),
            new TimeSlotWithoutIdDTO( "Word if The Day Report", "Bericht über die Verwendung desWortes des Abends", "1:00", "1:30", "2:00"),
            new TimeSlotWithoutIdDTO( "General Evaluator", "Gesamtbewertung des Clubmeetings & Feedback an die strukturgebenden Rollen", "3:00", "4:00", "5:00"),
            new TimeSlotWithoutIdDTO( "Toastmasters End", "Zusammenfassung, Feedback einholen", "1:00", "2:00", "3:00"),
            new TimeSlotWithoutIdDTO( "End of Meeting", "Ende des Meetings", null, null, "1:00"))
    );


    public final TimeSlotRepository timeSlotRepository;

    public List<TimeSlotResponseDTO> getAllTimeSlots(){
        return timeSlotRepository.findAll()
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }


    public TimeSlotResponseDTO getTimeSlotById(String id){

        TimeSlot timeSlot = timeSlotRepository.
                findById(id).
                orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot with id " + id + WAS_NOT_FOUND));


        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
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
        BackendMapper backendMapper = new BackendMapper();
        List<TimeSlot> timeSlotLists = new ArrayList<>();
        for (TimeSlotWithoutIdDTO timeSlotWithoutIdDTO: timeSlotWithoutIdDTOList
             ) {
            timeSlotLists.add(backendMapper.mapToTimeSlotWithoutIdDTO(timeSlotWithoutIdDTO));

        }
        return timeSlotLists;
    }

    public TimeSlotResponseDTO addTimeSlot(TimeSlotWithoutIdDTO timeSlotWithoutIdDTO) {


        TimeSlot timeSlot = TimeSlot.builder()
                .title(timeSlotWithoutIdDTO.getTitle())
                .description(timeSlotWithoutIdDTO.getDescription())
                .green(timeSlotWithoutIdDTO.getGreen())
                .amber(timeSlotWithoutIdDTO.getAmber())
                .red(timeSlotWithoutIdDTO.getRed())
                .build();

        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlot);


        return TimeSlotResponseDTO.builder()
                .id(savedTimeSlot.getId())
                .title(savedTimeSlot.getTitle())
                .description(savedTimeSlot.getDescription())
                .green(savedTimeSlot.getGreen())
                .amber(savedTimeSlot.getAmber())
                .red(savedTimeSlot.getRed())
                .build();
    }

    public TimeSlotResponseDTO updateTimeSlot(TimeSlotWithoutIdDTO timeSlotWithoutIdDTO, String id) {
        TimeSlot timeSlotToUpdate = timeSlotRepository.
                findById(id).
                orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot with id  " + id + WAS_NOT_FOUND));
        timeSlotToUpdate.setTitle(timeSlotWithoutIdDTO.getTitle());
        timeSlotToUpdate.setDescription(timeSlotWithoutIdDTO.getDescription());
        timeSlotToUpdate.setGreen(timeSlotWithoutIdDTO.getGreen());
        timeSlotToUpdate.setAmber(timeSlotWithoutIdDTO.getAmber());
        timeSlotToUpdate.setRed(timeSlotWithoutIdDTO.getRed());

        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlotToUpdate);


        return TimeSlotResponseDTO.builder()
                .id(savedTimeSlot.getId())
                .title(savedTimeSlot.getTitle())
                .description(savedTimeSlot.getDescription())
                .green(savedTimeSlot.getGreen())
                .amber(savedTimeSlot.getAmber())
                .red(savedTimeSlot.getRed())
                .build();
    }
    public void deleteTimeSlot(String id) {
        TimeSlot timeSlot = timeSlotRepository
                .findById(id)
                .orElseThrow(() -> new TimeSlotNotFoundException("TimeSlot with id  " + id + WAS_NOT_FOUND));
        timeSlotRepository.delete(timeSlot);
    }

    public TimeSlotResponseDTO getTimeSlotByTitleAndRed(String title, String red) {

        TimeSlot timeSlot = timeSlotRepository.
                findByTitleAndRed(title, red).
                orElseThrow(() -> new TimeSlotNotFoundException("TimeSlo with title " + title + " red time " + red + WAS_NOT_FOUND));


        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
    }

    public TimeSlotResponseDTO getTimeSlotByTitleAndDescription(String title, String description) {

        TimeSlot timeSlot = timeSlotRepository.
                findByTitleAndDescription(title, description).
                orElseThrow(() -> new TimeSlotNotFoundException("TimeSlo with title " + title + " description " + description + WAS_NOT_FOUND));


        return TimeSlotResponseDTO.builder()
                .id(timeSlot.getId())
                .title(timeSlot.getTitle())
                .description(timeSlot.getDescription())
                .green(timeSlot.getGreen())
                .amber(timeSlot.getAmber())
                .red(timeSlot.getRed())
                .build();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByTitle(String titel) {
        return timeSlotRepository.findAllByTitleEqualsIgnoreCase(titel)
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByDescription(String description) {
        return timeSlotRepository.findAllByDescriptionEqualsIgnoreCase(description)
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByGreen(String green) {
        return timeSlotRepository.findAllByGreenEqualsIgnoreCase(green)
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();

    }

    public List<TimeSlotResponseDTO> getTimeSlotsByAmber(String amber) {
        return timeSlotRepository.findAllByAmberEqualsIgnoreCase(amber)
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }

    public List<TimeSlotResponseDTO> getTimeSlotsByRed(String red) {
        return timeSlotRepository.findAllByRedEqualsIgnoreCase(red)
                .stream()
                .map(timeSlot -> TimeSlotResponseDTO.builder()
                        .id(timeSlot.getId())
                        .title(timeSlot.getTitle())
                        .description(timeSlot.getDescription())
                        .green(timeSlot.getGreen())
                        .amber(timeSlot.getAmber())
                        .red(timeSlot.getRed())
                        .build())
                .toList();
    }
}