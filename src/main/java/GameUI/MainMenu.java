package GameUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.*;

public class MainMenu extends Application {
    private static SettingsMenu settings;
    private static Stage menuStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        menuStage = primaryStage;
        settings = new SettingsMenu(primaryStage);

        primaryStage.setTitle("Briscola - Main menu");

        Group menuGroup = new Group();

        createTitle(menuGroup);
        createButtons(menuGroup);

        Color background = Color.rgb(60, 124, 83);
        Scene MainScene = new Scene(menuGroup, 900, 500, background);
        primaryStage.setResizable(false);

        primaryStage.setScene(MainScene);
        primaryStage.show();
    }

    private void createTitle(Group target){
        // Title
        Text title = new Text(250, 200, "Briscola");
        title.setFill(Color.YELLOW);
        title.setStrokeWidth(2);
        title.setStroke(Color.BLACK);
        title.setFont(Font.font("verdana", 100));
        target.getChildren().add(title);

        // Subtitle
        Text subtitle = new Text(260, 230, "by Group 13: The Briscola Bois");
        subtitle.setFont(Font.font("verdana", FontWeight.BOLD, 10));
        target.getChildren().add(subtitle);
    }

    private void createButtons(Group target){
        double buttonWidth = 200, buttonHeight = 50;

        Button start = new Button("Start");
        start.setPrefSize(buttonWidth, buttonHeight);
        start.setLayoutY(400);
        start.setLayoutX(50);
        start.setOnAction(this::onStartPressed);
        target.getChildren().add(start);


        Button settings = new Button("Settings");
        settings.setPrefSize(buttonWidth, buttonHeight);
        settings.setLayoutY(400); // 100
        settings.setLayoutX(350); // 170
        settings.setOnAction(MainMenu::onSettingsPressed);
        target.getChildren().add(settings);

        Button exit = new Button("Exit");
        exit.setPrefSize(buttonWidth, buttonHeight);
        exit.setLayoutY(400); // 150
        exit.setLayoutX(650); // 170
        exit.setOnAction(MainMenu::onExitPressed);
        target.getChildren().add(exit);
    }
    private void onStartPressed(ActionEvent e){
        menuStage.hide();
        new Playfield(menuStage);
    }

    private static void onSettingsPressed(ActionEvent e){
        menuStage.hide();
        settings.showSettings();
    }

    private static void onExitPressed(ActionEvent e){
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}
