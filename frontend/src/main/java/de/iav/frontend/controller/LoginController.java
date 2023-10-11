package de.iav.frontend.controller;

import de.iav.frontend.model.User;
import de.iav.frontend.model.UserRequestDto;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

public class LoginController {
    private final UserService userService = UserService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    private static final Logger LOG = LogManager.getLogger();
    @FXML
    public PasswordField password;
    @FXML
    public TextField email;
    @FXML
    public Label informationForUser;
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private static final String GET_FIRSTNAME = "";
    private static final String GET_LASTNAME = "";
    @FXML
    public Button bSpecialLogin;

    public void loginButtonPressed(ActionEvent actionEvent) throws IOException {

        loginAuthorized(actionEvent);
    }

    private void loginAuthorized(ActionEvent actionEvent) throws IOException {

        if(isLoginDataValid()){
            LOG.info("loginVersuch");
            if (authService.login(email.getText(), password.getText())) {
                informationForUser.setText("login successfully");
                User logInUser = userService.getUserByEmail(email.getText());
                LOG.info("logInUser: {}" , logInUser);
                sceneSwitchService.switchToTimeSlotsController(actionEvent, logInUser);
                LOG.info("scene Switch : {}",  logInUser);
            } else {
                informationForUser.setText("login unsuccessfully");
            }
        }
        else {
            informationForUser.setText("login Daten ungültig");
        }

    }

    public void registerButtonPressed(ActionEvent actionEvent) throws IOException {

        LOG.info("RegisterButtonPressed: drin");
        try {
            LOG.info(userService.getUserByEmail(email.getText()) != null);
            LOG.info("RegisterButtonPressed: direkt vor if schleife");
            if (userService.getUserByEmail(email.getText()) != null) {
                LOG.info("User existiert und kann mit der mail nicht registriert werden");
                informationForUser.setText("This email already exists, sign in instead");
            } else {
                LOG.info("User existiert NICHT und kann mit der mail registriert werden");
                UserRequestDto userRequestDto = new UserRequestDto( GET_FIRSTNAME, GET_LASTNAME, email.getText(), password.getText());
                sceneSwitchService.switchToRegisterController(actionEvent, userRequestDto);
            }
        }
        catch (RuntimeException e) {
            LOG.info("RuntimeException: {}", e.getMessage());
            LOG.info("User existiert NICHT und kann mit der mail registriert werden");
            UserRequestDto userRequestDto = new UserRequestDto( GET_FIRSTNAME, GET_LASTNAME, email.getText(), password.getText());
            sceneSwitchService.switchToRegisterController(actionEvent, userRequestDto);
        }
    }


    public void onActionPasswordField(ActionEvent event) throws IOException {
        loginAuthorized(event);
    }


    public void onSpecialLoginButtonClick(ActionEvent event) throws IOException {
        email.setText("jaro.jenaro@speaker.de");
        password.setText("1234");
        loginAuthorized(event);
    }

    private boolean isLoginDataValid(){
        if (email.getText().isEmpty() || password.getText().isEmpty()) {
            return false;
        }
        else{
            if (!email.getText().contains("@"))
                return false;
            else if (email.getText().length() < 4) {
                return false;
            }
            else if (password.getText().length() < 4) {
                return false;
            }
            else return email.getText().contains(".");
        }
    }
}


