module GameUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires com.esotericsoftware.kryo;

    opens GameUI to javafx.fxml;

    exports GameUI;

    // For serialization purposes
    exports Game.Bots.ReinforcementLearning.Bloom;
    exports Game.Bots.ReinforcementLearning;
    exports Game.Cards;
    opens Game.Cards to com.esotericsoftware.kryo;
}
