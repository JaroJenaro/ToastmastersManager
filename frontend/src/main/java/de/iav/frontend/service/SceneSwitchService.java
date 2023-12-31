package de.iav.frontend.service;

import de.iav.frontend.controller.*;
import de.iav.frontend.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SceneSwitchService {

    private static SceneSwitchService instance;
    private static final Logger LOG = LogManager.getLogger();

    public static synchronized SceneSwitchService getInstance() {
        if (instance == null) {
            instance = new SceneSwitchService();
        }
        return instance;
    }

    public void switchToLoginController(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/Login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToRegisterController(ActionEvent actionEvent, UserRequestDto userRequestDto) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/Registration.fxml"));
        Parent root = loader.load();


        RegistrationController registrationController = loader.getController();
        LOG.info("  ----> public void switchToUserController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {}", userRequestDto);
        registrationController.setUserWithoutIdDtoForSignIn(userRequestDto);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUserEditController(ActionEvent actionEvent, User loggedUser, User selectedItem) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/Registration.fxml"));
        Parent root = loader.load();

        RegistrationController registrationController = loader.getController();
        LOG.info("  ----> public void switchToUserEditController(ActionEvent actionEvent, User user) throws IOException {}", loggedUser);
        registrationController.setUserToUpdate(loggedUser, selectedItem);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTimeSlotsController(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/TimeSlotsData.fxml"));
        Parent root = loader.load();

        TimeSlotsController timeSlotsController = loader.getController();
        LOG.info("  ----> public void switchToTimeSlotsController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        timeSlotsController.setUserToShow(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToNewTimeSlotEditController(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/TimeSlotEdit.fxml"));
        Parent root = loader.load();

        TimeSlotEditController newTimeSlotEditController = loader.getController();
        LOG.info("  ----> public void switchToTimeSlotEditController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        newTimeSlotEditController.setUserToShow(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTimeSlotEditController(ActionEvent actionEvent, User user, TimeSlot timeSlot) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/TimeSlotEdit.fxml"));
        Parent root = loader.load();

        TimeSlotEditController timeSlotEditController = loader.getController();
        LOG.info("  ----> public void switchToTimeSlotEditController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        timeSlotEditController.setUserToShow(user);
        timeSlotEditController.setTimeSlotToEdit(timeSlot);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUsersController(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/UsersData.fxml"));
        Parent root = loader.load();

        UsersController usersController = loader.getController();
        LOG.info("  ----> public void switchToUsersController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        usersController.setUserToShow(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    public void switchToSpeechContributionController(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/SpeechContributionController.fxml"));
        Parent root = loader.load();

        SpeechContributionController speechContributionController = loader.getController();
        LOG.info("  ----> public void switchToUsersController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        speechContributionController.setUserToShow(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void switchToSpeechContributionEditController(ActionEvent actionEvent, User user, SpeechContribution speechContribution, int meetingIndex) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/SpeechContributionEdit.fxml"));
        Parent root = loader.load();

        SpeechContributionEditController speechContributionEditController = loader.getController();
        LOG.info("  ----> public void switchToSpeechContributionEditController(ActionEvent actionEvent, User user, SpeechContribution speechContribution) throws IOException {} ", user);
        speechContributionEditController.setUserToShowAndSpeechContributionToUpdate(user, speechContribution, meetingIndex);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToMeetingController(ActionEvent actionEvent, User user, int meetingIndex) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/MeetingController.fxml"));
        Parent root = loader.load();

        MeetingController meetingController = loader.getController();
        LOG.info("  ----> public void switchToMeetingController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        meetingController.setUserAndMeetingToShow(user, meetingIndex);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreateMeetingController(ActionEvent actionEvent, User user,  int meetingIndex, int meetingsSize) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/CreateOrEditMeeting.fxml"));
        Parent root = loader.load();

        CreateOrEditMeetingController createOrEditMeetingController = loader.getController();
        LOG.info("  ----> public void switchToCreateMeetingController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        createOrEditMeetingController.setUserAndEditToShow(user, meetingIndex, meetingsSize);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToUpdateMeetingController(ActionEvent actionEvent, User user, Meeting meeting, int meetingIndex) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/CreateOrEditMeeting.fxml"));
        Parent root = loader.load();

        CreateOrEditMeetingController createOrEditMeetingController = loader.getController();
        LOG.info("  ----> public void switchToCreateMeetingController(ActionEvent actionEvent, User user) throws IOException {} ", user);
        createOrEditMeetingController.setUserAndUpdateToShow(user, meeting, meetingIndex);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}