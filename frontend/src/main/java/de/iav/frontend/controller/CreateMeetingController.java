package de.iav.frontend.controller;

import de.iav.frontend.model.Meeting;
import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.model.User;
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
import java.util.ArrayList;
import java.util.List;

public class CreateMeetingController {
    private final MeetingService meetingService = MeetingService.getInstance();
    private final TimeSlotService timeSlotService = TimeSlotService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();

    private static final String CREATE_NOT_POSSIBLE = "Erstellen nicht möglich";

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
    private User loggedUser;
    ObservableList<String> locationList = FXCollections.observableArrayList();

    public void initialize() {
        LOG.info("---->CreateMeetingController public void initialize");
        locationList.add("Braunschweig");
        locationList.add("Berlin");
        locationList.add("Hannover");
        cbLocation.setItems(locationList);
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToTimeSlotsController(event,loggedUser);
    }

    public void onCreateButtonClick() throws InterruptedException {
        try {
            if (checkDate() && checkLocation()) {
                Meeting meetingExist = meetingService.getMeetingByDateTimeAndLocation(lDateTime.getText(), cbLocation.getValue());
                if (meetingExist == null) {
                    List<TimeSlot> timeSlots = timeSlotService.getAllTimeSlots();

                    Meeting meeting = new Meeting(null, lDateTime.getText(), cbLocation.getValue(), getSpeechContributionList(timeSlots));
                    Meeting savedMeeting = meetingService.createMeeting(meeting, authService.getSessionId());
                    Alerts.getMessageBoxWithInformationAndOkButton("Erfolgreiches anlegen des Meetings mit der ID",
                            "Erfolgreiches anlegen des Meetings mit der ID: " + savedMeeting.id(), "alles geklappt");
                } else {
                    Alerts.getMessageBoxWithWarningAndOkButton(
                            CREATE_NOT_POSSIBLE,
                            "Anlegen eines Meetings nicht möglich weil bereits ein existiert mit ID: " + meetingExist.id(),
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

    public void setUserToShow(User user) {
        loggedUser= user;
    }

    private boolean checkDate()
    {
        LocalDate selectedDate = dpDateTime.getValue();
        String timeText = tfTime.getText();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if (selectedDate != null && !timeText.isEmpty()) {
            LocalDateTime dateTime = LocalDateTime.of(selectedDate, LocalTime.parse(timeText, timeFormatter));
            DateTimeFormatter combinedFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm 'Uhr'");
            String combinedText = dateTime.format(combinedFormatter);
            lDateTime.setText(combinedText);
            return true;
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
}