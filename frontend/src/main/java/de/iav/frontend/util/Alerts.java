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

    public static void getMessageBoxWithConfirmationAndOkButton(String  title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                LOG.info("Pressed OK.");
            }
        });
    }

    public static void getMessageBoxWithInformationAndOkButton(String  title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                LOG.info("Pressed OK.");
            }
        });
    }

    public static boolean getMessageBoxWithInformationAndOkButtonAndReturnBoolean(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert.showAndWait().filter(rs -> rs == ButtonType.OK).isPresent();
    }
}