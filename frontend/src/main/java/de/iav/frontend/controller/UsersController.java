package de.iav.frontend.controller;

import de.iav.frontend.model.User;
import de.iav.frontend.security.AuthService;
import de.iav.frontend.service.SceneSwitchService;
import de.iav.frontend.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class UsersController {
    private final UserService userService = UserService.getInstance();
    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    private final AuthService authService = AuthService.getInstance();


    private static final Logger LOG = LogManager.getLogger();
    @FXML
    public ListView<User> lvUsers;
    @FXML
    public Label lLoggedInUser;
    private User loggedUser;

    public void initialize() {
        LOG.info("---->UsersController public void initialize");
        showAllUsers();
    }

    private void showAllUsers() {
        lvUsers.getItems().clear();
        LOG.info("showAllUsers");

        lvUsers.getItems().addAll(userService.getAllUsers());
        LOG.info("showAllUsers durch");
    }



    public void onEditUserClick(ActionEvent event) {
        try
        {
            if(lvUsers.getSelectionModel().getSelectedItem() != null){
                sceneSwitchService.switchToUserEditController(event, loggedUser, lvUsers.getSelectionModel().getSelectedItem());
            }
            else {
                noUserIsSelectedMessageBox("Damit ein User bearbeitet werden kann muss zunächst einer selektiert werden.");

            }
        }
        catch(Exception e)
        {
            noUserIsSelectedMessageBox("Damit ein User bearbeitet werden kann muss zunächst einer selektiert werden." +e.getMessage());
        }


    }

    public void onDeleteUserClick() {
        try
        {
            if(lvUsers.getSelectionModel().getSelectedItem() != null){
                userService.deleteUser(lvUsers.getSelectionModel().getSelectedItem().id(), lvUsers, authService.getSessionId());
            }
            else {
                noUserIsSelectedMessageBox("Damit ein User gelöscht werden kann muss zunächst einer selektiert werden.");

            }
        }
        catch(Exception e)
        {
            noUserIsSelectedMessageBox("Damit ein User gelöscht werden kann muss zunächst einer selektiert werden." +e.getMessage());
        }


    }

    public void setUserToShow(User user) {
        loggedUser= user;
        lLoggedInUser.setText(authService.me());
    }

    private void noUserIsSelectedMessageBox(String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kein User ausgewählt");
        alert.setHeaderText("Siehe Details in Information Dialog");
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                LOG.info("Pressed OK.");
            }
        });
    }
}
