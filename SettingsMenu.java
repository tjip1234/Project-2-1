package GameUI;

import Game.Cards.Card;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SettingsMenu extends Stage {
    private final Stage menuStage;

    public SettingsMenu(Stage menuStage){
        this.menuStage = menuStage;

        setTitle("Briscola - Settings");
        Group settingsGroup = new Group();

        createSettingEntries(settingsGroup);
        createButtons(settingsGroup);

        Color background = Color.rgb(60, 124, 83);
        Scene settingScene = new Scene(settingsGroup, 540, 240, background);
        setResizable(false);

        this.setScene(settingScene);

        setOnCloseRequest(e -> returnControl());
    }

    public void showSettings(){
        numberOfPlayers.getSelectionModel().clearSelection();
        skinChoice.getSelectionModel().clearSelection();
        botChoice.getSelectionModel().clearSelection();

        show();
    }

    private ComboBox<Integer> numberOfPlayers;
    private ComboBox<CardTextureStore.CardSkin> skinChoice;

    private ComboBox<String> botChoice;

    private void createSettingEntries(Group target){
        numberOfPlayers = new ComboBox<>();
        numberOfPlayers.setPromptText("Choose the amount of players");
        numberOfPlayers.setPrefSize(220, 50); // w:240
        numberOfPlayers.setLayoutY(20);
        numberOfPlayers.setLayoutX(25); //150
        numberOfPlayers.getItems().add(2);
        numberOfPlayers.getItems().add(4);
        numberOfPlayers.getItems().add(6);
        numberOfPlayers.setButtonCell(new PromptListCell<>("Choose the amount of players"));
        target.getChildren().add(numberOfPlayers);

        skinChoice = new ComboBox<>();
        skinChoice.setPromptText("Choose a skin");

        for(var skin : CardTextureStore.CardSkin.values())
            skinChoice.getItems().add(skin);

        skinChoice.setPrefSize(220, 50);
        skinChoice.setLayoutY(20); //90
        skinChoice.setLayoutX(300); //150
        skinChoice.setButtonCell(new PromptListCell<>("Choose a skin"));

        target.getChildren().add(skinChoice);

        botChoice = new ComboBox<>();
        botChoice.setPromptText("Choose Bot Type");
        botChoice.setPrefSize(220, 50);
        botChoice.setLayoutY(90);
        botChoice.setLayoutX(25);
        botChoice.getItems().add("Random Bot");
        botChoice.getItems().add("Greedy Bot");
        botChoice.getItems().add("Monte Carlo Tree Search");
        botChoice.setButtonCell(new PromptListCell<>("Choose Bot Type"));
        target.getChildren().add(botChoice);

    }

    private void createButtons(Group target){
        Button OkButton = new Button("Ok");
        OkButton.setPrefSize(240, 50);
        OkButton.setLayoutY(160);
        OkButton.setLayoutX(20);
        OkButton.setOnAction(e -> {
            commitChanges();
            returnControl();
        });
        target.getChildren().add(OkButton);

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(240, 50);
        cancelButton.setLayoutY(160);
        cancelButton.setLayoutX(280);
        cancelButton.setOnAction(e -> returnControl());
        target.getChildren().add(cancelButton);
    }

    private void commitChanges(){
        if(numberOfPlayers.getValue() != null)
            BriscolaConfigs.setPlayerNumber(numberOfPlayers.getValue());

        if(skinChoice.getValue() != null)
            BriscolaConfigs.setSkin(skinChoice.getValue());

        if(botChoice.getValue() != null)
            BriscolaConfigs.setBot(botChoice.getValue());
    }

    private void returnControl(){
        this.hide();
        menuStage.show();
    }

    private static class PromptListCell<T> extends ListCell<T>{
        private final String prompt;

        public PromptListCell(String promptText){
            prompt = promptText;
        }

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty) ;
            if (empty || item == null) {
                setText(prompt);
            } else {
                setText(item.toString());
            }
        }
    }
}
