package de.iav.frontend;

import de.iav.frontend.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    private String userData;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application! " );
    }

    public void setUserToShow(User user) {
        userData = user.toString();
    }
}