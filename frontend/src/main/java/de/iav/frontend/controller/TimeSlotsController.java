package de.iav.frontend.controller;

import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.User;
import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.SpeechContributionService;
import de.iav.frontend.service.TimeSlotService;
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
                noTimeSlotIsSelectedMessageBox("Damit ein Timeslot bearbeitet werden kann muss zunächst einer selektiert werden.");

            }
        } catch (Exception e) {
            noTimeSlotIsSelectedMessageBox("Damit ein Timeslot bearbeitet werden kann muss zunächst einer selektiert werden." + e.getMessage());
        }
    }

    public void onDeleteTimeSlotClick() {
        try {
            if (lvTimeSlots.getSelectionModel().getSelectedItem() != null) {
                timeSlotService.deleteTimeSlot(lvTimeSlots.getSelectionModel().getSelectedItem().id(), lvTimeSlots, authService.getSessionId());
            } else {
                noTimeSlotIsSelectedMessageBox("Damit ein Timeslot gelöscht werden kann muss zunächst einer selektiert werden.");

            }
        } catch (Exception e) {
            noTimeSlotIsSelectedMessageBox("Damit ein Timeslot gelöscht werden kann muss zunächst einer selektiert werden." + e.getMessage());
        }
    }

    public void setUserToShow(User user) {
        loggedUser = user;
    }

    private void noTimeSlotIsSelectedMessageBox(String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kein TimeSlot ausgewählt");
        alert.setHeaderText("Siehe Details in Information Dialog");
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                LOG.info("Pressed OK.");
            }
        });
    }

    private void getMessageBoxWithConfirmationAndOkButton(String  title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                LOG.info("Pressed OK.");
            }
        });
    }

    public void onUsersDataButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToUsersController(event, loggedUser);
    }

    public void onLogoutButtonClick(ActionEvent event) throws IOException {
        if (authService.logout()) {
            sceneSwitchService.switchToLoginController(event);
        } else
            noTimeSlotIsSelectedMessageBox("logout nicht erfolgreich");
    }

    public void onAddSpeechContributionClick() {
        try {
            if (lvTimeSlots.getSelectionModel().getSelectedItem() != null) {
                SpeechContribution savedSpeechContribution =  speechContributionService.createSpeechContribution (
                        new SpeechContribution(null,lvTimeSlots.getSelectionModel().getSelectedItem(), loggedUser, null),
                        authService.getSessionId()
                );
                getMessageBoxWithConfirmationAndOkButton("Erfolgreich",
                        "Redebeitrag mit der id:" + savedSpeechContribution.id() + "erfolgreich angelegt", "alles super");
            } else {
                noTimeSlotIsSelectedMessageBox("Damit ein Redebeitrag angelegt werden kann muss zunächst ein Timeslot selektiert werden.");

            }
        } catch (Exception e) {
            noTimeSlotIsSelectedMessageBox("Damit ein Redebeitrag angelegt werden kann muss zunächst ein Timeslot selektiert werden." + e.getMessage());
        }
    }

    public void onShowSpeechContributionButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToSpeechContributionController(event, loggedUser);
    }
}