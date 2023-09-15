package de.iav.frontend.controller;
//import de.iav.frontend.model.TransactionWithoutUser;

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

    @FXML
    public void onSignUpButtonClick(ActionEvent event) throws IOException {
        //userWithoutIdDto = new UserWithoutIdDto(firstName.getText(), lastName.getText(), email.getText(), password.getText());
        //sceneSwitchService.switchToPortfolioScene(event, userService.addUser(userWithoutIdDto));
        register(event);

    }

    public void backButtonPressed(ActionEvent actionEvent) throws IOException {
        sceneSwitchService.switchToLoginController(actionEvent);
    }

    public void setUserWithoutIdDtoForSignIn(UserWithoutIdDto userWithoutIdDto) {
        email.setText(userWithoutIdDto.email());
        password.setText(userWithoutIdDto.password());
        firstName.setText(userWithoutIdDto.firstName());
        lastName.setText(userWithoutIdDto.lastName());
    }


    public void register(ActionEvent event) throws IOException {

        AppUserRequest appUserRequest = new AppUserRequest(
                firstName.getText(),
                lastName.getText(),
                email.getText(),
                password.getText()
        );

        if (authService.registerAppUser(appUserRequest)) {
            sceneSwitchService.switchToLoginController(event);
            System.out.println(appUserRequest);


        } else {
            errorLabel.setText(authService.getErrorMessage());
        }
    }
}














