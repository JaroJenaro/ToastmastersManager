package de.iav.frontend.controller;

import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.User;
import de.iav.frontend.security.AuthService;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SpeechContributionController {
    private final SpeechContributionService speechContributionService = SpeechContributionService.getInstance();
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
    private User loggedUser;
    ObservableList<SpeechContribution> speechContributionList = FXCollections.observableArrayList();

    public void initialize() {
        LOG.info("---->UsersController public void initialize");
        showAllSpeechContributions();
    }

    private void showAllSpeechContributions() {
        LOG.info("showAllSpeechContributions");

        tvSpeechContribution.getColumns().clear();
        speechContributionList.addAll(speechContributionService.getAllSpeechContributions());

        tcTitle.setCellValueFactory(cellData -> {
            String title =cellData.getValue().timeSlot().title();
            return Bindings.createObjectBinding(() -> title);
        });

        tcLastName.setCellValueFactory(cellData -> {
            String description =cellData.getValue().timeSlot().description();
            return Bindings.createObjectBinding(() -> description);
        });

        tcEmail.setCellValueFactory(cellData -> {
            String red =cellData.getValue().timeSlot().red();
            return Bindings.createObjectBinding(() -> red);
        });
        tcFirsteName.setCellValueFactory(cellData -> {
            User user =cellData.getValue().user();
            return Bindings.createObjectBinding(() ->user!=null?user.toString():"keinRedner");
        });

        TableColumn<SpeechContribution, Integer> indexColumn = new TableColumn<>("#");
        indexColumn.setCellValueFactory(param -> new ReadOnlyIntegerWrapper(tvSpeechContribution.getItems().indexOf(param.getValue()) + 1).asObject());
        indexColumn.setSortable(false);
        indexColumn.setPrefWidth(30); // Breite der Indexspalte

        tvSpeechContribution.getColumns().add(indexColumn);

        tvSpeechContribution.getColumns().add(tcTitle);
        tvSpeechContribution.getColumns().add(tcLastName);
        tvSpeechContribution.getColumns().add(tcEmail);
        tvSpeechContribution.getColumns().add(tcFirsteName);

        // Daten in die TableView einfügen
        tvSpeechContribution.setItems(speechContributionList);

        LOG.info("showAllSpeechContributions durch");
    }

    public void setUserToShow(User user) {
        loggedUser= user;
        lLoggedInUser.setText(authService.me());
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
            Alerts.getMessageBoxWithWarningAndOkButton(DELETE_NOT_POSSIBLE, "Zum Löschen selektieren Sie bitte einen Redebeitrag", "ohne dies geht es hier nicht weiter");
        }
        } catch (Exception e) {
            Alerts.getMessageBoxWithWarningAndOkButton(DELETE_NOT_POSSIBLE, "Löschen nicht möglich, weile siehe unten: ", e.getMessage());
        }
    }

    public void onEditSpeechContributionClick(ActionEvent event) throws IOException {
        if(tvSpeechContribution.getSelectionModel().getSelectedItem() != null)
        {
            sceneSwitchService.switchToSpeechContributionEditController(event, loggedUser, tvSpeechContribution.getSelectionModel().getSelectedItem());

        }
        else {
            Alerts.getMessageBoxWithWarningAndOkButton(UPDATE_NOT_POSSIBLE, "Zum Bearbeiten selektieren Sie bitte einen Redebeitrag", "ohne dies geht es hier nicht weiter");
        }
    }
}