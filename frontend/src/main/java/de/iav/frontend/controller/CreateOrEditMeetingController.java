package de.iav.frontend.controller;

import de.iav.frontend.model.*;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.MeetingService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.TimeSlotService;
import de.iav.frontend.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CreateOrEditMeetingController {
    private final MeetingService meetingService = MeetingService.getInstance();
    private final TimeSlotService timeSlotService = TimeSlotService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();
    private static final String CREATE_NOT_POSSIBLE = "Erstellen nicht möglich";
    private static final String UPDATE_NOT_POSSIBLE = "Aktualisieren nicht möglich";
    private static final String DATE_TIME_FORMAT = "yyyy.MM.dd HH:mm 'Uhr'";
    private static final String TIME_FORMAT = "HH:mm";

    @FXML
    public Label lLoggedInUser;
    @FXML
    public Button bBack;
    @FXML
    public Label lMeetingTitle;
    @FXML
    public ComboBox<String> cbLocation;
    @FXML
    public Button bUpdate;
    @FXML
    public DatePicker dpDateTime;
    @FXML
    public Label lDateTime;
    @FXML
    public TextField tfTime;
    @FXML
    public Label lLocation;
    @FXML
    public Button bCreate;

    private User loggedUser;
    private WayToCreateOrEdit wayToCreateOrEdit;
    private final ObservableList<String> locationList = FXCollections.observableArrayList();
    private Meeting meetingToUpdate;
    private int meetingIndex;
    private int meetingSize;

    public void initialize() {
        LOG.info("---->CreateMeetingController public void initialize");
        locationList.add("Braunschweig");
        locationList.add("Berlin");
        locationList.add("Hannover");
        locationList.add("Hamburg");
        locationList.add("Magdeburg");
        locationList.add("Wolfsburg");
        locationList.add("Goslar");
        cbLocation.setItems(locationList);
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToMeetingController(event, loggedUser, meetingIndex);
    }

    public void onCreateButtonClick(ActionEvent event) throws InterruptedException {
        if(wayToCreateOrEdit == WayToCreateOrEdit.CREATE){
            createOneMeeting(event);
        }
        else if(wayToCreateOrEdit == WayToCreateOrEdit.UPDATE){
            updateOneMeeting(event);
        }
        else{
            Alerts.getMessageBoxWithWarningAndOkButton(
                    "Irgendwas ist schief gelaufen",
                    "Weder Create noch Update Modus gesetzt. Bitte prüfen aus welche Stelle der Controller aufgerufen wird",
                    "Weder die   Methode setUserAndEditToShow(User user) noch  setUserAndUpdateToShow(User user) " +
                            "werden beim Aufruf des Controllers genutzt. Dies ist aber für die Funktionsweise erforderlich");
        }
    }

    private void createOneMeeting(ActionEvent event) throws InterruptedException {
        try {
            if (checkDate() && checkLocation()) {
                Meeting meetingExists = meetingService.getMeetingByDateTimeAndLocation(lDateTime.getText(), cbLocation.getValue());
                if (meetingExists == null) {
                    List<TimeSlot> timeSlots = timeSlotService.getAllTimeSlots();

                    Meeting meeting = new Meeting(null, lDateTime.getText(), cbLocation.getValue(), getSpeechContributionList(timeSlots));
                    Meeting savedMeeting = meetingService.createMeeting(meeting, authService.getSessionId());
                    if(Alerts.getMessageBoxWithInformationAndOkButtonAndReturnBoolean("Erfolgreiches anlegen des Meetings mit der ID",
                            "Erfolgreiches anlegen des Meetings mit der ID: " + savedMeeting.id(), "alles geklappt"))
                        sceneSwitchService.switchToMeetingController(event, loggedUser, meetingSize);
                } else {
                    Alerts.getMessageBoxWithWarningAndOkButton(
                            CREATE_NOT_POSSIBLE,
                            "Anlegen eines Meetings nicht möglich weil bereits ein existiert mit ID: " + meetingExists.id(),
                            "kommt später");
                }
            }
        } catch (IOException e) {
            Alerts.getMessageBoxWithWarningAndOkButton(CREATE_NOT_POSSIBLE, "Hat leider nicht geklappt. IOException Siehe Meldung:", e.getMessage());
        } catch (InterruptedException e) {
            Alerts.getMessageBoxWithWarningAndOkButton(CREATE_NOT_POSSIBLE, "Hat leider nicht geklappt. InterruptedException Siehe Meldung:", e.getMessage());
            throw e;
        }
    }

    private void updateOneMeeting(ActionEvent event) throws InterruptedException {
        try {
            if (checkDate() && checkLocation()) {
                Meeting meetingExists = meetingService.getMeetingByDateTimeAndLocation(meetingToUpdate.dateTime(), meetingToUpdate.location());
                if (meetingExists != null) {

                    Meeting meeting = new Meeting(meetingToUpdate.id(), lDateTime.getText(), cbLocation.getValue(), meetingToUpdate.speechContributionList());
                    Meeting savedMeeting = meetingService.updateMeeting(meeting, authService.getSessionId());
                    if(Alerts.getMessageBoxWithInformationAndOkButtonAndReturnBoolean("Erfolgreiches Aktualisieren des Meetings mit der ID",
                            "Erfolgreiches Aktualisieren des Meetings mit der ID: " + savedMeeting.id(), "alles geklappt"))
                        sceneSwitchService.switchToMeetingController(event, loggedUser, meetingIndex);
                } else {
                    Alerts.getMessageBoxWithWarningAndOkButton(
                            UPDATE_NOT_POSSIBLE,
                            "Aktualisieren eines Meetings nicht möglich weil ein Meeting nicht existiert!",
                            "kommt später");
                }
            }
        } catch (IOException e) {
            Alerts.getMessageBoxWithWarningAndOkButton(CREATE_NOT_POSSIBLE, "Hat leider nicht geklappt. IOException Siehe Meldung:", e.getMessage());
        } catch (InterruptedException e) {
            Alerts.getMessageBoxWithWarningAndOkButton(CREATE_NOT_POSSIBLE, "Hat leider nicht geklappt. InterruptedException Siehe Meldung:", e.getMessage());
            throw e;
        }
    }

    private List<SpeechContribution> getSpeechContributionList(List<TimeSlot> timeSlots)
    {
        List<SpeechContribution> speechContribution = new ArrayList<>();
        for (TimeSlot timeSlot:timeSlots) {
            speechContribution.add(new SpeechContribution(null, timeSlot, null, null));
        }
        return speechContribution;
    }

    public void setUserAndEditToShow(User user, int meetingIndex, int meetingSize) {
        loggedUser= user;
        meetingToUpdate = null;
        this.meetingIndex = meetingIndex;
        this.meetingSize = meetingSize;
        this.wayToCreateOrEdit = WayToCreateOrEdit.CREATE;
        bCreate.setVisible(true);
        bUpdate.setVisible(false);
    }

    public void setUserAndUpdateToShow(User user, Meeting meeting, int meetingIndex) {
        loggedUser= user;
        meetingToUpdate = meeting;
        this.meetingIndex = meetingIndex;
        cbLocation.setValue(meetingToUpdate.location());
        dpDateTime.setValue(getLocalDateFromMeetingDateTime(meetingToUpdate.dateTime()));
        tfTime.setText(getTimeFromMeetingDateTime(meetingToUpdate.dateTime()));
        lDateTime.setText(meetingToUpdate.dateTime());

        this.wayToCreateOrEdit = WayToCreateOrEdit.UPDATE;
        bCreate.setVisible(false);
        bUpdate.setVisible(true);
    }

    private String getTimeFromMeetingDateTime(String timeString) {
        //das hier auch mit try absichern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalTime localTime = LocalTime.parse(timeString, formatter);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
        return localTime.format(outputFormatter);
    }

    private LocalDate getLocalDateFromMeetingDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDate.parse(dateString, formatter);
    }

    private boolean checkDate()
    {
        LocalDate selectedDate = dpDateTime.getValue();
        String timeText = tfTime.getText();
        if (selectedDate != null && (timeText.length() == 5)) {
            return onDateTimeCheckIntern();
        } else {
            lDateTime.setText("Bitte wähle ein Datum und gib eine Uhrzeit ein.");
            return false;
        }
    }

    private boolean checkLocation()
    {
        if(cbLocation.getSelectionModel().getSelectedItem() != null)
            return true;
        else{
            lLocation.setText("Bitte wähle ein Standort aus.");
            return false;
        }
    }

    public void onDateTimeCheck() {
        onDateTimeCheckIntern();
    }

    private boolean onDateTimeCheckIntern()
    {
        try{
            LocalDate selectedDate = dpDateTime.getValue();
            String timeText = tfTime.getText();

            if (selectedDate != null && (timeText.length() == 5)){
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(TIME_FORMAT);
                LocalDateTime dateTime = LocalDateTime.of(selectedDate, LocalTime.parse(timeText, timeFormatter));
                DateTimeFormatter combinedFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
                String combinedText = dateTime.format(combinedFormatter);
                lDateTime.setText(combinedText);
            }
            return true;
        }
        catch(DateTimeParseException e)
        {
            Alerts.getMessageBoxWithWarningAndOkButton("Ungültige Angaben", "Hat leider nicht geklappt. DateTimeParseException Siehe Meldung:", e.getMessage());
        }
        return false;
    }
}