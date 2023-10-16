package de.iav.frontend.controller;

import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.User;
import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.SpeechContributionService;
import de.iav.frontend.service.TimeSlotService;
import de.iav.frontend.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TimeSlotsController {
    private final TimeSlotService timeSlotService = TimeSlotService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final SpeechContributionService speechContributionService = SpeechContributionService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();

    @FXML
    public ListView<TimeSlot> lvTimeSlots;
    @FXML
    public Label lLoggedInUser;
    @FXML
    public Button bUserData;
    @FXML
    public Button bLogout;
    @FXML
    public Button bAddCSpeechContribution;
    @FXML
    public Button bShowSpeechContribution;
    @FXML
    public Button bFirstMeeting;
    @FXML
    public Button bCreateOneMeeting;
    private User loggedUser;

    public void initialize() {
        LOG.info("---->TimeSlotsController public void initialize");
        showAllTimeSlots();
        lLoggedInUser.setText(authService.me());
    }

    private void showAllTimeSlots() {
        lvTimeSlots.getItems().clear();
        LOG.info("showAllTimeSlots");

        lvTimeSlots.getItems().addAll(timeSlotService.getAllTimeSlots());
        LOG.info("showAllTimeSlots durch");
    }

    public void onNewTimeSlotClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToNewTimeSlotEditController(event, loggedUser);
    }

    public void onEditTimeSlotClick( ActionEvent event) {
        try {
            if (lvTimeSlots.getSelectionModel().getSelectedItem() != null) {
                sceneSwitchService.switchToTimeSlotEditController(event, loggedUser, lvTimeSlots.getSelectionModel().getSelectedItem());

            } else {
                Alerts.getMessageBoxWithInformationAndOkButton("Kein TimeSlot zum Bearbeiten ausgewählt", "Siehe Details zum Bearbeiten in Information Dialog",
                        "Damit ein Timeslot bearbeitet werden kann muss zunächst einer selektiert werden.");

            }
        } catch (Exception e) {
            Alerts.getMessageBoxWithInformationAndOkButton("Kein TimeSlot zum Bearbeiten ausgewählt", "Siehe Details zum Edit in Information Dialog",
                    "Damit ein Timeslot bearbeitet werden kann muss zunächst einer selektiert werden." + e.getMessage());
        }
    }

    public void onDeleteTimeSlotClick() {
        try {
            if (lvTimeSlots.getSelectionModel().getSelectedItem() != null) {
                timeSlotService.deleteTimeSlot(lvTimeSlots.getSelectionModel().getSelectedItem().id(), lvTimeSlots, authService.getSessionId());
            } else {
                Alerts.getMessageBoxWithInformationAndOkButton("Kein TimeSlot zum Löschen ausgewählt", "Siehe Details zum Löschen in Information Dialog",
                        "Damit ein Timeslot gelöscht werden kann muss zunächst einer selektiert werden.");

            }
        } catch (Exception e) {
            Alerts.getMessageBoxWithInformationAndOkButton("Unerwartetes Verhalten", "Siehe Details zum Delete in Information Dialog",
                    "Damit ein Timeslot gelöscht werden kann muss zunächst einer selektiert werden." + e.getMessage());
        }
    }

    public void setUserToShow(User user) {
        loggedUser = user;
    }

    public void onUsersDataButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToUsersController(event, loggedUser);
    }

    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        if (authService.logout()) {
            sceneSwitchService.switchToLoginController(event);
        } else
            Alerts.getMessageBoxWithInformationAndOkButton("Logout nicht erfolgreich", "Siehe Details zum Logout in Information Dialog",
                    "logout nicht erfolgreich");
    }

    public void onAddSpeechContributionClick() {
        try {
            if (lvTimeSlots.getSelectionModel().getSelectedItem() != null) {
                SpeechContribution savedSpeechContribution =  speechContributionService.createSpeechContribution (
                        new SpeechContribution(null,lvTimeSlots.getSelectionModel().getSelectedItem(), loggedUser, null),
                        authService.getSessionId()
                );
                Alerts.getMessageBoxWithConfirmationAndOkButton("Erfolgreich",
                        "Redebeitrag mit der id:" + savedSpeechContribution.id() + "erfolgreich angelegt", "alles super");
            } else {
                Alerts.getMessageBoxWithInformationAndOkButton("Kein Redebeitrag erstellt.", "Siehe Details zum Redebeitrag in Information Dialog",
                        "Damit ein Redebeitrag angelegt werden kann muss zunächst ein Timeslot selektiert werden.");

            }
        } catch (Exception e) {
            Alerts.getMessageBoxWithInformationAndOkButton("Kein TimeSlot ausgewählt", "Siehe Details in Information Dialog",
                    "Damit ein Redebeitrag angelegt werden kann muss zunächst ein Timeslot selektiert werden." + e.getMessage());
        }
    }

    public void onShowSpeechContributionButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToSpeechContributionController(event, loggedUser);
    }

    public void onFirstMeetingButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToMeetingController(event, loggedUser);
    }

    public void onCreateOneMeetingButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToCreateMeetingController(event, loggedUser);
    }
}