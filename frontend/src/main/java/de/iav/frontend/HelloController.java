package de.iav.frontend;

import de.iav.frontend.model.User;
import de.iav.frontend.service.SceneSwitchService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class HelloController {
    @FXML
    public TextArea userText;
    @FXML
    private Label welcomeText;
    private String userData;

    private final SceneSwitchService sceneSwitchService = SceneSwitchService.getInstance();
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application! : \n user: " + userData  );
        userText.setText("Welcome to JavaFX Application! : \n user: " + userData);
    }

    public void setUserToShow(User user) {
        userData = user.toString();
    }

    public void onBack2LoginButtonClick(ActionEvent event) throws IOException {
        sceneSwitchService.switchToLoginController(event);
    }
}