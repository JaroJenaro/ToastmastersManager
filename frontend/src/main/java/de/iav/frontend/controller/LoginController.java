
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

        import java.io.IOException;
// jaro.jenaro@speaker.de
public class LoginController {
    private final UserService userService = UserService.getInstance();
    private final AuthService authService = AuthService.getInstance();
    @FXML
    public PasswordField password;
    @FXML
    public TextField email;
    @FXML
    public Label informationForUser;
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();

    public void signInButtonPressed(ActionEvent actionEvent) throws IOException {

        loginAuthorized(actionEvent);
    }

    private void loginAuthorized(ActionEvent actionEvent) throws IOException {
        System.out.println("loginversuch");
        if (authService.login(email.getText(), password.getText())) {
            informationForUser.setText("login successfully");
            User logInUser = userService.getUserByEmail(email.getText());
            System.out.println("logInUser: " + logInUser);
            sceneSwitchService.switchToHelloController(actionEvent, logInUser);
            System.out.println("scene Switch : " + logInUser);
        } else {
            informationForUser.setText("login unsuccessfully");
        }

    }

    public void signUpButtonPressed(ActionEvent actionEvent) throws IOException {

        System.out.println(userService.getUserByEmail(email.getText()) != null);
        if (userService.getUserByEmail(email.getText()) != null) {
            informationForUser.setText("This email already exists, sign in instead");
        } else {
            UserWithoutIdDto userWithoutIdDto = new UserWithoutIdDto("getName", "getName", email.getText(), password.getText());
            sceneSwitchService.switchToRegisterController(actionEvent, userWithoutIdDto);


        }
    }

    public void deleteUser() {

    }


    public void onActionPasswordField(ActionEvent event) throws IOException {
        loginAuthorized(event);
    }
}


