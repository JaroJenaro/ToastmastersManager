package de.iav.frontend.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Alerts {
    Alerts() {
    }

    private static final Logger LOG = LogManager.getLogger();

    public static void getMessageBoxWithWarningAndOkButton(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                LOG.info("Pressed OK in INFORMATION.");
            }
        });
    }
}
