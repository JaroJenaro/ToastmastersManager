package de.iav.frontend.service;

import de.iav.frontend.controller.*;
import de.iav.frontend.model.User;
import de.iav.frontend.HelloController;
import de.iav.frontend.controller.RegistrationController;
import de.iav.frontend.model.UserWithoutIdDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitchService {

    private static SceneSwitchService instance;

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

    // das hier zum RegisterController umbenennen
    public void switchToRegisterController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/controller/user.fxml"));
        Parent root = loader.load();


        RegistrationController registrationController = loader.getController();
        System.out.println("  ----> public void switchToUserController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {: " + userWithoutIdDto);
        registrationController.setUserWithoutIdDtoForSignIn(userWithoutIdDto);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHelloController(ActionEvent actionEvent, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/de/iav/frontend/hello-view.fxml"));
        Parent root = loader.load();


        HelloController helloController = loader.getController();
        System.out.println("  ----> public void switchToUserController(ActionEvent actionEvent, UserWithoutIdDto userWithoutIdDto) throws IOException {: " + user);
        helloController.setUserToShow(user);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }








}
