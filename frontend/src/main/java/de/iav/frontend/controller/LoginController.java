
package de.iav.frontend.controller;

        import de.iav.frontend.model.User;
        import de.iav.frontend.model.UserWithoutIdDto;
        import de.iav.frontend.security.AuthService;
        import de.iav.frontend.service.SceneSwitchService;
        import de.iav.frontend.service.UserService;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Label;
        import javafx.scene.control.PasswordField;
        import javafx.scene.control.TextField;
        import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;

        import java.io.IOException;
// jaro.jenaro@speaker.de
// kasia.kasia@wasnun.de
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
    private static final String GET_FIRSTNAME = "get Firtsname";
    private static final String GET_LASTNAME = "get Lastsname";
    public void loginButtonPressed(ActionEvent actionEvent) throws IOException {

        loginAuthorized(actionEvent);
    }

    private void loginAuthorized(ActionEvent actionEvent) throws IOException {
        LOG.info("loginversuch");
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
                UserWithoutIdDto userWithoutIdDto = new UserWithoutIdDto( GET_FIRSTNAME, GET_LASTNAME, email.getText(), password.getText());
                sceneSwitchService.switchToRegisterController(actionEvent, userWithoutIdDto);


            }
        }
        catch (RuntimeException e) {
            LOG.info("RuntimeException: {}", e.getMessage());
            LOG.info("User existiert NICHT und kann mit der mail registriert werden");
            UserWithoutIdDto userWithoutIdDto = new UserWithoutIdDto( GET_FIRSTNAME, GET_LASTNAME, email.getText(), password.getText());
            sceneSwitchService.switchToRegisterController(actionEvent, userWithoutIdDto);
        }
    }

    public void deleteUser() {
        // Wird später umgesetzt
    }


    public void onActionPasswordField(ActionEvent event) throws IOException {
        loginAuthorized(event);
    }


}


