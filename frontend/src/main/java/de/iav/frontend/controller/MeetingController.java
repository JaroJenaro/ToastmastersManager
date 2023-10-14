package de.iav.frontend.controller;

import de.iav.frontend.model.Meeting;
import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.User;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.MeetingService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.util.Alerts;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class MeetingController {
    private final MeetingService meetingService = MeetingService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();

    private static final String DELETE_NOT_POSSIBLE = "Löschen nicht möglich";
    private static final String UPDATE_NOT_POSSIBLE = "Bearbeiten nicht möglich";
    @FXML
    public Label lLoggedInUser;
    @FXML
    public Button bBack;
    @FXML
    public TableView<SpeechContribution> tvSpeechContribution;
    @FXML
    public TableColumn<SpeechContribution, String> tcFirsteName;
    @FXML
    public TableColumn<SpeechContribution, String> tcTitle;
    @FXML
    public TableColumn<SpeechContribution, String>  tcLastName;
    @FXML
    public TableColumn<SpeechContribution, String>  tcEmail;

    @FXML
    public Label lLocation;
    @FXML
    public Label lDateTime;
    @FXML
    private User loggedUser;
    ObservableList<SpeechContribution> speechContributionList = FXCollections.observableArrayList();
    List<Meeting> meetingsList;

    public void initialize() {
        LOG.info("---->UsersController public void initialize");
        showAllSpeechContributionsInMeeting();
    }

    private void showAllSpeechContributionsInMeeting() {
        LOG.info("showAllSpeechContributions");

        tvSpeechContribution.getColumns().clear();
        tcFirsteName.setCellValueFactory(cellData -> {
            String firstName =cellData.getValue().user().firstName();
            return Bindings.createObjectBinding(() -> firstName);
        });

        tcLastName.setCellValueFactory(cellData -> {
            String lastName =cellData.getValue().user().lastName();
            return Bindings.createObjectBinding(() -> lastName);
        });

        tcEmail.setCellValueFactory(cellData -> {
            String email =cellData.getValue().user().email();
            return Bindings.createObjectBinding(() -> email);
        });

        tcTitle.setCellValueFactory(cellData -> {
            String title =cellData.getValue().timeSlot().title();
            return Bindings.createObjectBinding(() -> title);
        });
        tvSpeechContribution.getColumns().add(tcFirsteName);
        tvSpeechContribution.getColumns().add(tcLastName);
        tvSpeechContribution.getColumns().add(tcEmail);
        tvSpeechContribution.getColumns().add(tcTitle);
        LOG.info("showAllSpeechContributions durch");
    }

    public void setUserAndMeetingToShow(User user) {
        loggedUser= user;
        lLoggedInUser.setText(authService.me());
        meetingsList = meetingService.getAllMeetings();
        if (!meetingsList.isEmpty()) {
            lDateTime.setText(meetingsList.get(0).dateTime());
            lLocation.setText(meetingsList.get(0).location());
            // Daten in die TableView einfügen
            speechContributionList.addAll(meetingsList.get(0).speechContributionList());
            tvSpeechContribution.setItems(speechContributionList);
        }
        else Alerts.getMessageBoxWithWarningAndOkButton("Kein Meeting in DB", "DB enthält keinen Meeting",
                "legen sie zunächst einen Termin ein!" );
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToTimeSlotsController(event,loggedUser);
    }

    public void onDeleteSpeechContributionClick() {
        Alerts.getMessageBoxWithWarningAndOkButton(DELETE_NOT_POSSIBLE, "Löschen noch nicht umgesetzt", "kommt später");
    }

    public void onEditSpeechContributionClick() {
        Alerts.getMessageBoxWithWarningAndOkButton(UPDATE_NOT_POSSIBLE, "Bearbeiten noch nicht umgesetzt", "kommt später");
    }
}