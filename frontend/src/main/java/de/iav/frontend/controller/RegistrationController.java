package de.iav.frontend.controller;

import de.iav.frontend.model.UserWithoutIdDto;
import de.iav.frontend.security.AppUserRequest;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RegistrationController {
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField email;
    @FXML
    public PasswordField password;
    @FXML
    public Button signUp;
    @FXML
    public Label errorLabel;


    private final AuthService authService = AuthService.getInstance();

    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private static final Logger LOG = LogManager.getLogger();
    @FXML
    public void onSignUpButtonClick(ActionEvent event) throws IOException {
      register(event);
    }

    public void backButtonPressed(ActionEvent actionEvent) throws IOException {
        sceneSwitchService.switchToLoginController(actionEvent);
    }

    public void setUserWithoutIdDtoForSignIn(UserWithoutIdDto userWithoutIdDto) {
        LOG.info("setUserWithoutIdDtoForSignIn drin");
        email.setText(userWithoutIdDto.email());
        LOG.info("setUserWithoutIdDtoForSignIn mail");
        password.setText(userWithoutIdDto.password());
        LOG.info("setUserWithoutIdDtoForSignIn password");
        firstName.setText(userWithoutIdDto.firstName());
        LOG.info("setUserWithoutIdDtoForSignIn firstname");
        lastName.setText(userWithoutIdDto.lastName());
        LOG.info("setUserWithoutIdDtoForSignIn lastname");
    }


    public void register(ActionEvent event) throws IOException {

        if (isRegisterDataValid()) {

            AppUserRequest appUserRequest = new AppUserRequest(
                    firstName.getText(),
                    lastName.getText(),
                    email.getText(),
                    password.getText()
            );

            if (authService.registerAppUser(appUserRequest)) {
                sceneSwitchService.switchToLoginController(event);
                LOG.info(appUserRequest);
            } else {
                errorLabel.setText(authService.getErrorMessage());
            }
        }
        else {
            errorLabel.setText("Bitte alle Felder ausfüllen" );
        }
    }

    private boolean isRegisterDataValid(){
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty()||
                email.getText().isEmpty() || password.getText().isEmpty()) {
            return false;
        }
        else{
            if (!email.getText().contains("@"))
                return false;
            else if (email.getText().length() < 4) {
                return false;
            }
            else if (firstName.getText().length() < 4) {
                return false;
            }
            else if (lastName.getText().length() < 4) {
                return false;
            }
            else if (password.getText().length() < 4) {
                return false;
            }
            else return email.getText().contains(".");
        }
    }
}














