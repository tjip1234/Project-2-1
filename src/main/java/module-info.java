module GameUI {
    requires javafx.controls;
    requires javafx.fxml;

    opens GameUI to javafx.fxml;
    exports GameUI;
}