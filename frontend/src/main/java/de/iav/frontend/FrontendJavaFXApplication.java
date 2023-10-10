package de.iav.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontendJavaFXApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FrontendJavaFXApplication.class.getResource("/de/iav/frontend/controller/Login.fxml"));
        //prefHeight="232.0" prefWidth="408.0"
        Scene scene = new Scene(fxmlLoader.load(), 420, 250);
        stage.setTitle("ToastMasters");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}