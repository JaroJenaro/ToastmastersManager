package de.iav.frontend.controller;

import de.iav.frontend.model.*;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.SpeechContributionService;
import de.iav.frontend.service.UserService;
import de.iav.frontend.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SpeechContributionEditController {

    @FXML
    public Label lSpeechContributionTitle;
    @FXML
    public Button bUpdate;
    @FXML
    public ComboBox<User> cbUser;
    @FXML
    public Label errorLabel;

    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private final SpeechContributionService speechContributionService = SpeechContributionService.getInstance();
    private static final Logger LOG = LogManager.getLogger();

    @FXML
    public Label lTimeSlot;

    ObservableList<User> usersList = FXCollections.observableArrayList();
    private User loggedUser;

    private SpeechContribution speechContributionToUpdate;

    public void initialize() {
        LOG.info("SpeechContributionEditController");
   }

    public void backButtonPressed(ActionEvent actionEvent) throws IOException {
        sceneSwitchService.switchToSpeechContributionController(actionEvent, loggedUser);
    }

    public void onUpdateButtonClick(ActionEvent event) throws IOException {
        if((speechContributionToUpdate.user() != null) && speechContributionToUpdate.user().equals(cbUser.getSelectionModel().getSelectedItem()))
        {
            Alerts.getMessageBoxWithWarningAndOkButton("Keine Änderung", "Es ist kein neue User ausgewählt.", "Deshalb macht Speichern keinen Sinn");
            errorLabel.setText("Zum Bearbeiten eines Beitrags dan user ändern. Ansonsten Abbrechen.");
        }
        else
        {
            SpeechContribution speechContributionToSave = new SpeechContribution(speechContributionToUpdate.id(),speechContributionToUpdate.timeSlot(),
                    cbUser.getSelectionModel().getSelectedItem(), speechContributionToUpdate.stoppedTime());
            SpeechContribution savedSpeechContribution = speechContributionService.updateSpeechContribution(speechContributionToSave, authService.getSessionId());

            LOG.info("Updated SpeechContribution: {}", savedSpeechContribution);
            sceneSwitchService.switchToSpeechContributionController(event, loggedUser);
        }
    }

    public void setUserToShowAndSpeechContributionToUpdate(User user, SpeechContribution speechContribution) {
        loggedUser= user;
        speechContributionToUpdate = speechContribution;

        lTimeSlot.setText(speechContributionToUpdate.timeSlot().toString());
        usersList.addAll(userService.getAllUsers());
        cbUser.setItems(usersList);
        cbUser.setValue(speechContributionToUpdate.user());

    }
}