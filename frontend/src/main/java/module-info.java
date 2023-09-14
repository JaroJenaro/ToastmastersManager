module de.iav.frontend {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
                            
    opens de.iav.frontend to javafx.fxml;
    exports de.iav.frontend;
}