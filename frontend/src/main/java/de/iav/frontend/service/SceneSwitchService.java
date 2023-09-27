package de.iav.frontend.service;

import de.iav.frontend.controller.*;
import de.iav.frontend.model.TimeSlot;
import de.iav.frontend.model.User;
import de.iav.frontend.HelloController;
import de.iav.frontend.model.UserWithoutIdDto;
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

    public void switchToRegisterController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/Registration.fxml"));
        Parent root = loader.load();


        RegistrationController registrationController = loader.getController();
        LOG.info("  ----> public void switchToUserController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {}", userWithoutIdDto);
        registrationController.setUserWithoutIdDtoForSignIn(userWithoutIdDto);

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

    public void switchToHelloController(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/hello-view.fxml"));
        Parent root = loader.load();


        HelloController helloController = loader.getController();
        LOG.info("  ----> public void switchToUserController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {}", user);
        helloController.setUserToShow(user);

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
}
