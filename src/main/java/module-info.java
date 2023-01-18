module GameUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.esotericsoftware.kryo;

    opens GameUI to javafx.fxml;

    exports Game.Bots.ReinforcementLearning.Bloom.V2;

    exports GameUI;
}
