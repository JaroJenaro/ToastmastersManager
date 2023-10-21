package de.iav.frontend.controller;

import de.iav.frontend.model.Role;
import de.iav.frontend.model.User;
import de.iav.frontend.model.UserRequestDto;
import de.iav.frontend.model.WayToCreateOrEditUser;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class RegistrationController {
    @FXML
    public Label lRole;
    @FXML
    public ComboBox<Role> cbRole;
    @FXML
    public Label lPassword;
    @FXML
    public Label lRegisterTitle;
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField email;
    @FXML
    public PasswordField password;
    @FXML
    public Button bSignUp;
    @FXML
    public Label errorLabel;

    private final AuthService authService = AuthService.getInstance();
    private final UserService userService = UserService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private static final Logger LOG = LogManager.getLogger();
    @FXML
    public Button bUpdate;
    @FXML
    public Button bUserRead;
    @FXML
    public Button bBackToLogin;

    private User loggedUser;
    private User userToUpdate;

    private WayToCreateOrEditUser wayToCreateOrEditUser;

    public void initialize() {
        firstName.setPromptText("Vorname mind 4 Zeichen Lang");
        lastName.setPromptText("Nachname mind 4 Zeichen Lang");
        password.setPromptText("Password mind 4 Zeichen Lang");
        email.setPromptText("email mind 4 Zeichen Lang x@l.de");
        LOG.info("---->RegistrationController public void initialize");
    }

    @FXML
    public void onSignUpButtonClick(ActionEvent event) throws IOException {
        if(wayToCreateOrEditUser==WayToCreateOrEditUser.REGISTER)
            registerUser(event);

    }

    private void updateUser(ActionEvent event) throws IOException {
        if (isUpdateDataValid()) {
            User user = new User(
                    userToUpdate.id(),
                    firstName.getText(),
                    lastName.getText(),
                    email.getText(),
                    cbRole.getValue()
            );
            if (isUserUpdateSuccessful(user,userService.updateUser(user, authService.getSessionId()))) {
                LOG.info("updateUser: {}", user);
                sceneSwitchService.switchToUsersController(event, loggedUser);
            } else {
                errorLabel.setText(authService.getErrorMessage());
            }
        }else {
            errorLabel.setText("Bitte alle Felder ausfüllen" );
        }


    }

    private boolean isUserUpdateSuccessful(User userIn, User userOut)
    {
        return userIn.equals(userOut);
    }


    public void setUserWithoutIdDtoForSignIn(UserRequestDto userRequestDto ) {
        this.wayToCreateOrEditUser = WayToCreateOrEditUser.REGISTER;
        LOG.info("setUserWithoutIdDtoForSignIn drin");
        email.setText(userRequestDto.email());
        LOG.info("setUserWithoutIdDtoForSignIn mail");
        password.setText(userRequestDto.password());
        LOG.info("setUserWithoutIdDtoForSignIn password");
        firstName.setText(userRequestDto.firstName());
        LOG.info("setUserWithoutIdDtoForSignIn firstname");
        lastName.setText(userRequestDto.lastName());
        LOG.info("setUserWithoutIdDtoForSignIn lastname");
        cbRole.setVisible(false);
        lRole.setVisible(false);

        bUpdate.setVisible(false);
        bSignUp.setVisible(true);

        bUserRead.setVisible(false);
        bBackToLogin.setVisible(true);

        lRegisterTitle.setText("Register User");

    }

    public void registerUser(ActionEvent event) throws IOException {
        if (isRegisterDataValid()) {
            UserRequestDto appUserRequest = new UserRequestDto(
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

    private boolean isUpdateDataValid(){
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty()||
                email.getText().isEmpty()) {
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
            else return email.getText().contains(".");
        }
    }

    public void setUserToUpdate(User loggedUser, User user) {
        this.wayToCreateOrEditUser = WayToCreateOrEditUser.UPDATE;
        userToUpdate = user;
        this.loggedUser = loggedUser;
        LOG.info("setUserWithoutIdDtoForSignIn drin");
        email.setText(user.email());
        LOG.info("setUserWithoutIdDtoForSignIn mail");
        firstName.setText(user.firstName());
        LOG.info("setUserWithoutIdDtoForSignIn firstname");
        lastName.setText(user.lastName());
        LOG.info("setUserWithoutIdDtoForSignIn lastname");
        cbRole.getItems().addAll(Role.USER, Role.ADMIN);
        cbRole.setValue(user.role());
        password.setVisible(false);
        lPassword.setVisible(false);

        bUpdate.setVisible(true);
        bSignUp.setVisible(false);

        bUserRead.setVisible(true);
        bBackToLogin.setVisible(false);

        lRegisterTitle.setText(loggedUser.toString());
    }

    public void onUpdateButtonClick(ActionEvent event) throws IOException {
        if (wayToCreateOrEditUser==WayToCreateOrEditUser.UPDATE) {
            updateUser(event);
        }
    }

    public void backToLoginButtonPressed(ActionEvent event) throws IOException {
        sceneSwitchService.switchToLoginController(event);
    }

    public void onUserReadButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToUsersController(event, loggedUser);
    }
}