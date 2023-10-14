package de.iav.frontend.controller;

import de.iav.frontend.model.SpeechContribution;
import de.iav.frontend.model.User;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.SpeechContributionService;
import de.iav.frontend.util.Alerts;
import javafx.beans.binding.Bindings;
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
        // TODO
    }

    public void onEditSpeechContributionClick(ActionEvent event) throws IOException {
        if(tvSpeechContribution.getSelectionModel().getSelectedItem() != null)
        {
            sceneSwitchService.switchToSpeechContributionEditController(event, loggedUser, tvSpeechContribution.getSelectionModel().getSelectedItem());

        }
        else {
            Alerts.getMessageBoxWithWarningAndOkButton("Nichts selektiert", "selektieren Sie bitte einen Redebeitrag", "ohne dies geht es hier nicht weiter");
        }

    }


}