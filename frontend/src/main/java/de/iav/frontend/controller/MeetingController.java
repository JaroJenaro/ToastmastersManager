package de.iav.frontend.controller;

import de.iav.frontend.model.Meeting;
import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.User;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.MeetingService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.SpeechContributionService;
import de.iav.frontend.util.Alerts;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

public class MeetingController {
    private final MeetingService meetingService = MeetingService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final SpeechContributionService speechContributionService = SpeechContributionService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();

    private static final String DELETE_NOT_POSSIBLE = "Löschen nicht möglich";
    private static final String UPDATE_NOT_POSSIBLE = "Bearbeiten nicht möglich";

    @FXML
    public Label lLoggedInUser;
     @FXML
    public TableView<SpeechContribution> tvSpeechContribution;
    @FXML
    public  TableColumn<SpeechContribution, String> tcTimeSlotTitle;
    @FXML
    public  TableColumn<SpeechContribution, String> tcTimeSlotRed;
    @FXML
    public  TableColumn<SpeechContribution, String> tcUserLastAndFirstName;
    @FXML
    public  TableColumn<SpeechContribution, String> tcTimeSlotDescription;
    @FXML
    public Label lLocation;
    @FXML
    public Label lDateTime;
    @FXML
    public Button bPrev;
    @FXML
    public Button bNext;
    @FXML
    public Label lNavi;
    @FXML
    public Button bUpdateMeeting;
    @FXML
    public Label lPlaceHolder;
    @FXML
    public Button bCreateMeeting;
    @FXML
    public Button bDeleteMeeting;
    @FXML
    private User loggedUser;

    private Integer meetingIndex;
    private final ObservableList<SpeechContribution> speechContributionList = FXCollections.observableArrayList();
    private List<Meeting> meetingsList;

    public void initialize() {
        LOG.info("---->UsersController public void initialize");
        showAllSpeechContributionsInMeeting();
    }

    private void showAllSpeechContributionsInMeeting() {
        LOG.info("showAllSpeechContributions");

        tvSpeechContribution.getColumns().clear();

        tcTimeSlotTitle.setCellValueFactory(cellData -> {
            String title =cellData.getValue().timeSlot().title();
            return Bindings.createObjectBinding(() -> title);
        });
        tcTimeSlotRed.setCellValueFactory(cellData -> {
            String red =cellData.getValue().timeSlot().red();
            return Bindings.createObjectBinding(() -> red);
        });
        tcTimeSlotDescription.setCellValueFactory(cellData -> {
            String description =cellData.getValue().timeSlot().description();
            return Bindings.createObjectBinding(() -> description);
        });
        tcUserLastAndFirstName.setCellValueFactory(cellData -> {
            User user =cellData.getValue().user();
            return Bindings.createObjectBinding(() ->user!=null?user.lastName() + ", " + user.firstName():" ");
        });

        // Spalte für die Indexe hinzufügen
        TableColumn<SpeechContribution, Integer> indexColumn = new TableColumn<>("#");
        indexColumn.setCellValueFactory(param -> new ReadOnlyIntegerWrapper(tvSpeechContribution.getItems().indexOf(param.getValue()) + 1).asObject());
        indexColumn.setSortable(false);
        indexColumn.setPrefWidth(30);

        TableColumn<SpeechContribution, String> idColumn = new TableColumn<>("id");
        idColumn.setCellValueFactory(cellData -> {
            String idSpeechContribution =cellData.getValue().id();
            return Bindings.createObjectBinding(() -> idSpeechContribution);
        });

        tvSpeechContribution.getColumns().add(indexColumn);
        tvSpeechContribution.getColumns().add(tcTimeSlotTitle);
        tvSpeechContribution.getColumns().add(tcTimeSlotRed);
        tvSpeechContribution.getColumns().add(tcUserLastAndFirstName);
        tvSpeechContribution.getColumns().add(tcTimeSlotDescription);
        tvSpeechContribution.getColumns().add(idColumn);
        LOG.info("showAllSpeechContributions durch");
    }

    public void setUserAndMeetingToShow(User user, int meetingIndex) {
        loggedUser= user;
        this.meetingIndex = meetingIndex;
        lLoggedInUser.setText(authService.me() + " Role: " +  user.role());
        meetingsList = meetingService.getAllMeetings();
        if (!meetingsList.isEmpty()) {
            updateTableAndOtherView();
            if (meetingsList.size() == 1){
                bNext.setDisable(true);
                bPrev.setDisable(true);
            }
            if(meetingIndex ==0){
                bPrev.setDisable(true);
            }
            if(meetingIndex == meetingsList.size()-1){
                bNext.setDisable(true);
            }
        }
        else {
            Alerts.getMessageBoxWithWarningAndOkButton("Kein Meeting in DB", "DB enthält keinen Meeting",
                    "legen sie zunächst einen Termin ein!" );
            bNext.setDisable(true);
            bPrev.setDisable(true);
        }
    }

    public void onBackButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToTimeSlotsController(event,loggedUser);
    }

    public void onDeleteSpeechContributionClick() {
        try {
            if(tvSpeechContribution.getSelectionModel().getSelectedItem() != null)
            {
                speechContributionService.deleteSpeechContribution(tvSpeechContribution.getSelectionModel().getSelectedItem().id(), tvSpeechContribution, authService.getSessionId());

            }
            else {
                Alerts.getMessageBoxWithWarningAndOkButton(DELETE_NOT_POSSIBLE, "Zum Löschen selektieren Sie bitte einen Redebeitrag", "Ohne Selektieren des Redebeitrags geht es hier nicht weiter");
            }
        } catch (Exception e) {
            Alerts.getMessageBoxWithWarningAndOkButton(DELETE_NOT_POSSIBLE, "Löschen nicht möglich, weile siehe unten: ", e.getMessage());
        }
    }

    public void onEditSpeechContributionClick(ActionEvent event) throws IOException {
        if(!tvSpeechContribution.getItems().isEmpty()){
            if(tvSpeechContribution.getSelectionModel().getSelectedItem() != null){
                sceneSwitchService.switchToSpeechContributionEditController(event, loggedUser, tvSpeechContribution.getSelectionModel().getSelectedItem(), meetingIndex);
            }else{
                Alerts.getMessageBoxWithWarningAndOkButton(UPDATE_NOT_POSSIBLE, "Zum Bearbeiten selektieren Sie bitte einen Redebeitrag", "ohne dies geht es hier nicht weiter");
            }
        }else{
            Alerts.getMessageBoxWithWarningAndOkButton(UPDATE_NOT_POSSIBLE, "Zum Bearbeiten muss ein Termin mit Redebeiträgen existieren", "ohne Meeting geht es hier nicht weiter");
        }
    }

    public void onPrevButtonClick() {
        if (meetingIndex > 0){
            meetingIndex--;
            bNext.setDisable(false);
            updateTableAndOtherView();
            if(meetingIndex == 0)
                bPrev.setDisable(true);
        }
        else Alerts.getMessageBoxWithWarningAndOkButton("Erste Index", "Erste Meeting",
                "hier sollte eigentlich graue button sein" );
    }

    public void onNextButtonClick() {
        if (meetingIndex < meetingsList.size()-1){
            meetingIndex++;
            bPrev.setDisable(false);
            updateTableAndOtherView();
            if(meetingIndex == meetingsList.size()-1)
                bNext.setDisable(true);
        }
        else Alerts.getMessageBoxWithWarningAndOkButton("Letzte Index", "Letzte  Meeting",
                "hier sollte eigentlich graue button sein" );
    }

    private void updateTableAndOtherView(){
        lDateTime.setText(meetingsList.get(meetingIndex).dateTime());
        lLocation.setText(meetingsList.get(meetingIndex).location());
        speechContributionList.clear();
        speechContributionList.addAll(meetingsList.get(meetingIndex).speechContributionList());
        tvSpeechContribution.setItems(speechContributionList);
        lNavi.setText((meetingIndex+1) + "/" + meetingsList.size() );
    }

    public void onUpdateMeetingButtonClick(ActionEvent event) throws IOException {
        if(!tvSpeechContribution.getItems().isEmpty()){
            sceneSwitchService.switchToUpdateMeetingController(event, loggedUser, meetingsList.get(meetingIndex), meetingIndex);
        }else{
            Alerts.getMessageBoxWithWarningAndOkButton(UPDATE_NOT_POSSIBLE, "Zum Bearbeiten muss ein Termin existieren", "ohne Meeting geht es hier nicht weiter");
        }
    }

    public void onRowTableViewMouseClicked(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            if(tvSpeechContribution.getSelectionModel().getSelectedItem() != null){
                ActionEvent actionEvent = new ActionEvent(mouseEvent.getSource(), mouseEvent.getTarget());
                sceneSwitchService.switchToSpeechContributionEditController(actionEvent, loggedUser, tvSpeechContribution.getSelectionModel().getSelectedItem(), meetingIndex);
            }
            else {
                Alerts.getMessageBoxWithWarningAndOkButton(UPDATE_NOT_POSSIBLE, "Zum Bearbeiten selektieren Sie bitte einen Redebeitrag", "ohne dies geht es hier nicht weiter");
            }
        }
    }
    public void onTableViewKeyPressed(KeyEvent keyEvent) {
        if ((keyEvent.getCode()== KeyCode.DELETE) && ! (tvSpeechContribution.getSelectionModel().isEmpty())) {
              SpeechContribution selectedSpeechContribution = tvSpeechContribution.getSelectionModel().getSelectedItem();
            Alerts.getMessageBoxWithWarningAndOkButton("Löschen wird demnächst umgesetzt", "An diese Stelle würden Sie folgenden Redebeitrag löschen",
                    "Id:" + selectedSpeechContribution.id() +
                            "\nTimeSlot:" + selectedSpeechContribution.timeSlot()  +
                            "\nUser:" + selectedSpeechContribution.user()
                    );
        }
    }

    public void onDeleteMeetingButtonClick() {
        Alerts.getMessageBoxWithWarningAndOkButton(DELETE_NOT_POSSIBLE, "Löschen noch nicht umgesetzt", "kommt später");
    }

    public void onCreateMeetingButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToCreateMeetingController(event, loggedUser, meetingIndex, meetingsList.size());
    }
}