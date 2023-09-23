package de.iav.frontend.controller;

import de.iav.frontend.model.User;
import de.iav.frontend.model.UserWithoutIdDto;
import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.TimeSlotService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeSlotsController {
    private final TimeSlotService timeSlotService = TimeSlotService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();

    //private static final Logger LOG = LogManager.getLogger();
    @FXML
    public ListView<TimeSlot> lv_timeSlots;
    @FXML
    public Label l_loggedInUser;

    public void initialize() {
        System.out.println("---->BuyViewController public void initialize");
        showAllTimeSlots();
    }

    private void showAllTimeSlots() {
        lv_timeSlots.getItems().clear();
        System.out.println("showAllTimeSlots");

        lv_timeSlots.getItems().addAll(timeSlotService.getAllTimeSlots());
        System.out.println("showAllTimeSlots durch");
    }

    public void onNewTimeSlotClick(ActionEvent event) {
    }

    public void onEditTimeSlotClick(ActionEvent event) {
    }

    public void onDeleteTimeSlotClick(ActionEvent event) {
    }

    public void setUserToShow(User user) {
        l_loggedInUser.setText(user.toString());
    }
}
