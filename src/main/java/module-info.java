module GameUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens GameUI to javafx.fxml;
    exports GameUI;
}