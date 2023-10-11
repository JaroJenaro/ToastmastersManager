package de.iav.frontend.controller;

import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.model.User;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.TimeSlotService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TimeSlotEditController {

    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final TimeSlotService timeSlotService = TimeSlotService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();
    @FXML
    public TextField tfTitle;
    @FXML
    public TextField tfDescription;
    @FXML
    public TextField tfGreen;
    @FXML
    public TextField tfAmber;
    @FXML
    public Button bSave;
    @FXML
    public Button bCancel;
    @FXML
    public TextField tfRed;
    @FXML
    public Label lLoggedInUser;
    @FXML
    public Label lId;

    private User loggedUser;


    public void initialize() {
        tfTitle.setPromptText("mind. 4 Zeichen");
        tfDescription.setPromptText("mind. 10 Zeichen");
        tfGreen.setPromptText("0:00, darf auch leer sein");
        tfAmber.setPromptText("0:00, darf auch leer sein");
        tfRed.setPromptText("1:00, mindestens 4 Zeichen Zeitangabe");

        LOG.info("---->public void initialize(");
        LOG.info("---->TimeSlotEditController public void initialize");

    }

    @FXML
    public void onSaveButtonClick(ActionEvent event) throws IOException {

        if(isTimeSlotDataValid()) {
            TimeSlot timeSlotToSave = getNewDataOfTimeSlot();
            TimeSlot savedTimeSlot;
            if (timeSlotToSave.id().isEmpty()) {
                LOG.error("Versuch neu zur erstellen: {}", timeSlotToSave);
                savedTimeSlot = timeSlotService.createTimeSlot(timeSlotToSave, authService.getSessionId());
            } else {
                LOG.error("Versuch zu updaten: {}", timeSlotToSave);
                savedTimeSlot = timeSlotService.updateTimeSlot(timeSlotToSave, authService.getSessionId());
            }

            LOG.info("Gespeicherte timeSlot {}", savedTimeSlot);
            sceneSwitchService.switchToTimeSlotsController(event, loggedUser);
        }
        else {
            lLoggedInUser.setText("Ungültige Daten");
        }
    }

    @FXML
    public void onCancelButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToTimeSlotsController(event,loggedUser);
    }


    public void setUserToShow(User user) {
        loggedUser= user;
        lLoggedInUser.setText(user.toString());
    }
    public void setTimeSlotToEdit(TimeSlot timeSlotToEdit)
    {
        if (!timeSlotToEdit.id().isEmpty())
        {
            showAllTimeSlotData(timeSlotToEdit);
        }
    }

    private void showAllTimeSlotData(TimeSlot timeSlotToEdit) {
        tfTitle.setText(timeSlotToEdit.title());
        tfDescription.setText(timeSlotToEdit.description());
        tfGreen.setText(timeSlotToEdit.green());
        tfAmber.setText(timeSlotToEdit.amber());
        tfRed.setText(timeSlotToEdit.red());
        lId.setText(timeSlotToEdit.id());
    }

    private TimeSlot getNewDataOfTimeSlot() {
       return new TimeSlot(lId.getText(), tfTitle.getText(), tfDescription.getText(),
                tfGreen.getText(), tfAmber.getText(), tfRed.getText());
    }

    private boolean isTimeSlotDataValid(){
        if (tfTitle.getText().isEmpty() || tfDescription.getText().isEmpty()||
                tfRed.getText().isEmpty()){
            return false;
        }
        else{
            if (!tfRed.getText().contains(":"))
                return false;
            else if (tfRed.getText().length() < 4) {
                return false;
            }
            else if (tfTitle.getText().length() < 4) {
                return false;
            }
            else return tfDescription.getText().length() >= 10;
        }
    }
}
