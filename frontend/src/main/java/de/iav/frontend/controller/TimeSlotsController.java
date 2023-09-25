package de.iav.frontend.controller;

import de.iav.frontend.model.User;
import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.TimeSlotService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class TimeSlotsController {
    private final TimeSlotService timeSlotService = TimeSlotService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();

    private static final Logger LOG = LogManager.getLogger();
    @FXML
    public ListView<TimeSlot> lvTimeSlots;
    @FXML
    public Label lLoggedInUser;
    private User loggedUser;

    public void initialize() {
        LOG.info("---->TimeSlotsController public void initialize");
        showAllTimeSlots();
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

    public void onEditTimeSlotClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToTimeSlotEditController(event, loggedUser, lvTimeSlots.getSelectionModel().getSelectedItem());
    }

    public void onDeleteTimeSlotClick(ActionEvent event) {
        // Wird später umgesetzt
    }

    public void setUserToShow(User user) {
        loggedUser= user;
        lLoggedInUser.setText(user.toString());
    }
}
