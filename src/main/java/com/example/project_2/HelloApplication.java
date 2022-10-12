package com.example.project_2;

import Cards.Card;
import Game.GameSession;
import Game.Player;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    private GameSession g = null;
    private ImageView[] iv = new ImageView[5];
    private ArrayList<ImageView> deck = new ArrayList<>();
    private Stage stage = new Stage();
    private EventHandler<javafx.scene.input.MouseEvent> eh1;
    private EventHandler<javafx.scene.input.MouseEvent> eh2;
    private EventHandler<javafx.scene.input.MouseEvent> eh3;
    private Rectangle[] rects = {new Rectangle(30, 250, 120, 200),new Rectangle(180, 250, 120, 200),new Rectangle(330, 250, 120, 200)};
    private Text score1= new Text();
    private Text score2= new Text();
    private Text currrentPlayer = new Text("Current player: ");
    private int ammountOfPLayers = 2;




    @Override
    public void start(Stage stage) throws IOException {
        Button play_match = new Button("Play match");
        Button exit = new Button("Exit");

        Group menuRoot = new Group();
        menuRoot.getChildren().add(play_match);

        exit.setPrefSize(200,50);
        exit.setLayoutY(150);
        exit.setLayoutX(170);
        play_match.setPrefSize(200,50);
        play_match.setLayoutY(50);
        play_match.setLayoutX(170);
        Button bMenu = new Button("Settings");
        bMenu.setPrefSize(200,50);
        bMenu.setLayoutY(100);
        bMenu.setLayoutX(170);
        menuRoot.getChildren().add(bMenu);



        menuRoot.getChildren().add(exit);
        Color background = Color.rgb(60, 124, 83);
        Scene MainScene = new Scene(menuRoot, 540, 280, background);

        Stage menuWindow = new Stage();
        menuWindow.setTitle("Second Stage");
        menuWindow.setScene(MainScene);
        EventHandler<ActionEvent> startGameFromMenu = new EventHandler<>() {
            public void handle(ActionEvent e)
            {
                try {
                    menuWindow.close();
                    restart(stage);
                } catch (MalformedURLException | FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        };
        EventHandler<ActionEvent> exit_event = new EventHandler<>() {
            public void handle(ActionEvent e)
            {
                menuWindow.close();
                cleanup(stage);
            }
        };
        exit.setOnAction(exit_event);

        play_match.setOnAction(startGameFromMenu);

        EventHandler<ActionEvent> settingsEvent = new EventHandler<>() {
            public void handle(ActionEvent e)
            {
                ComboBox<String> howManyPlayers = new ComboBox<String>();

                ComboBox<String> skin = new ComboBox<String>();
                skin.getItems().add("default");
                skin.setPromptText("Choose the skin");

                howManyPlayers.setPromptText("Choose the amount of players");
                howManyPlayers.getItems().add("2");
                howManyPlayers.getItems().add("4");
                howManyPlayers.getItems().add("6");


                Button save_and_exit = new Button("Save and exit");

                Group settingsRoot = new Group();
                settingsRoot.getChildren().add(howManyPlayers);
                skin.setPrefSize(240,50);
                skin.setLayoutY(150);
                skin.setLayoutX(150);
                howManyPlayers.setPrefSize(240,50);
                howManyPlayers.setLayoutY(100);
                howManyPlayers.setLayoutX(150);
                save_and_exit.setPrefSize(240,50);
                save_and_exit.setLayoutY(50);
                save_and_exit.setLayoutX(150);




                settingsRoot.getChildren().add(skin);
                settingsRoot.getChildren().add(save_and_exit);





                Scene settingScene = new Scene(settingsRoot, 540, 280);

                // New window (Stage)
                Stage menu = new Stage();
                menu.setScene(settingScene);

                menu.setTitle("Setting");
                menu.setScene(settingScene);

                EventHandler<ActionEvent> event = new EventHandler<>() {
                    public void handle(ActionEvent e)
                    {
                        try {
                            menu.close();
                            restart(stage);
                        } catch (MalformedURLException ex) {
                            ex.printStackTrace();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                EventHandler<ActionEvent> event2 = new EventHandler<>() {
                    public void handle(ActionEvent e)
                    {
                        menu.close();

                    }
                };
                EventHandler<ActionEvent> howManyPlayersEvent = new EventHandler<>() {
                    public void handle(ActionEvent e)
                    {
                        ammountOfPLayers = Integer.parseInt(howManyPlayers.getValue());
                    }
                };
                howManyPlayers.setOnAction(howManyPlayersEvent);
                save_and_exit.setOnAction(event2);
                menu.setScene(settingScene);

                menu.show();
            }
        };

        bMenu.setOnAction(settingsEvent);



        // Set position of second window, related to primary window.
        menuWindow.setX(200);
        menuWindow.setY(100);
        menuWindow.show();






     //     }
    }
    private void initGame(Stage stage) throws MalformedURLException, FileNotFoundException {
        EventHandler<javafx.scene.input.MouseEvent> eh1 = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                try {
                    iv[3].setImage(g.players.get(g.currentPlayer).getHand().get(0).getImage(false));
                } catch (FileNotFoundException | MalformedURLException e) {
                    //e.printStackTrace();
                }
                iv[3].setVisible(true);

                g.playTurn(g.players.get(g.currentPlayer).getHand().get(0));

                for(int i = 0; i < 3; i++) {
                    try {
                        iv[i].setImage(g.players.get(g.currentPlayer).getHand().get(i).getImage(true));
                    } catch (FileNotFoundException | IndexOutOfBoundsException | MalformedURLException e) {
                        //e.printStackTrace();
                        iv[i].setVisible(false);
                        rects[i].setVisible(false);
                        if(i==0){
                            gameOver(stage);
                        }
                    }
                }
                for (int i = g.deck.size()+2; i < deck.size(); i++) {
                    if(g.deck.isEmpty()){
                        deck.get(i).setVisible(false);
                    }

                    if (!deck.get(i).isVisible()) {
                        break;
                    }
                    deck.get(i).setVisible(false);

                }
                iv[3].setVisible(!g.Table.isEmpty());
                currrentPlayer.setText("Current player: " + (g.currentPlayer+1));
                if(!g.isTeamGame) {
                    score1.setText("Player 1: " + g.players.get(0).Score());
                    score2.setText("Player 2: " + g.players.get(1).Score());
                }
                else{
                    score1.setText("Team 1: " + g.getScoreForTeam(0));
                    score2.setText("Team 2: " + g.getScoreForTeam(1));


                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> eh2 = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                try {
                    iv[3].setImage((g.players.get(g.currentPlayer).getHand().get(0).getImage(false)));
                } catch (FileNotFoundException| IndexOutOfBoundsException  | MalformedURLException e) {
                    e.printStackTrace();
                }
                iv[3].setVisible(true);

                g.playTurn(g.players.get(g.currentPlayer).getHand().get(1));

                for(int i = 0; i < 3; i++) {
                    try {
                        iv[i].setImage((g.players.get(g.currentPlayer).getHand().get(i).getImage(true)));
                    } catch (FileNotFoundException| IndexOutOfBoundsException  | MalformedURLException e) {
                        //e.printStackTrace();
                        iv[i].setVisible(false);
                        rects[i].setVisible(false);
                        if(i==0){
                            gameOver(stage);
                        }


                    }
                }
                for (int i = g.deck.size()+2; i < deck.size(); i++) {
                    if(g.deck.isEmpty()){
                        deck.get(i).setVisible(false);
                    }
                    if (!deck.get(i).isVisible()) {
                        break;
                    }
                    deck.get(i).setVisible(false);

                }
                if(g.Table.isEmpty()){
                    iv[3].setVisible(false);
                }
                else{
                    iv[3].setVisible(true);

                }
                currrentPlayer.setText("Current player: " + (g.currentPlayer+1));
                if(!g.isTeamGame) {
                    score1.setText("Player 1: " + g.players.get(0).Score());
                    score2.setText("Player 2: " + g.players.get(1).Score());
                }
                else{
                    score1.setText("Team 1: " + g.getScoreForTeam(0));
                    score2.setText("Team 2: " + g.getScoreForTeam(1));


                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> eh3 = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                try {
                    iv[3].setImage((g.players.get(g.currentPlayer).getHand().get(0).getImage(false)));
                } catch (FileNotFoundException | IndexOutOfBoundsException | MalformedURLException e) {
                    //e.printStackTrace();
                }
                iv[3].setVisible(true);
                g.playTurn(g.players.get(g.currentPlayer).getHand().get(2));
                for(int i = 0; i < 3; i++) {
                    try {
                        iv[i].setImage(g.players.get(g.currentPlayer).getHand().get(i).getImage(true));
                    } catch (FileNotFoundException| IndexOutOfBoundsException | MalformedURLException e) {
                        //e.printStackTrace();
                        iv[i].setVisible(false);
                        rects[i].setVisible(false);
                        if(i==0){
                            gameOver(stage);
                        }


                    }
                }
                for (int i = g.deck.size()+2; i < deck.size(); i++) {
                    if(g.deck.isEmpty()){
                        deck.get(i).setVisible(false);
                    }
                    if (!deck.get(i).isVisible()) {
                        break;
                    }
                    deck.get(i).setVisible(false);

                }
                iv[3].setVisible(!g.Table.isEmpty());
                currrentPlayer.setText("Current player: " + (g.currentPlayer+1));
                if(!g.isTeamGame) {
                    score1.setText("Player 1: " + g.players.get(0).Score());
                    score2.setText("Player 2: " + g.players.get(1).Score());
                }
                else{
                    score1.setText("Team 1: " + g.getScoreForTeam(0));
                    score2.setText("Team 2: " + g.getScoreForTeam(1));


                }
            }
        };
        g = new GameSession(ammountOfPLayers);
        score1 = new Text(20, 20, "Player 1: " + g.players.get(0).Score());
        score2 = new Text(420, 20, "Player 2: " + g.players.get(1).Score());
        currrentPlayer = new Text(0, 50, "Current player: " + g.currentPlayer);
        g.startRound();
        // while (true) {
        Card.Number cardNumber = g.deck.getBriscola().number;
        Card.Suit briscsuit = g.deck.getBriscola().suit;
        System.out.println(briscsuit+" "+cardNumber);
        String userHome = System.getProperty("user.dir");;
        Image imageForFile = new Image(userHome+"\\PNG-cards-1.3/"+cardNumber+"_of_"+briscsuit+".png");
        Text suit = new Text(220, 20, "Briscola: " + g.deck.getBriscola().suit);

        deck.add(0, new ImageView(g.deck.get(0).getImage(true)));
        deck.get(0).setFitWidth(120);
        deck.get(0).setFitHeight(200);
        deck.get(0).setRotate(-90);
        deck.get(0).setX(330);
        deck.get(0).setY(80);

        deck.get(0).setPreserveRatio(true);

        deck.get(0).setVisible(true);

        for (int i = 1; i < g.deck.size(); i++) {


            deck.add(i, new ImageView(g.deck.get(i).getImage(false)));
            deck.get(i).setFitWidth(120);
            deck.get(i).setFitHeight(200);

            deck.get(i).setX(330+i);
            deck.get(i).setY(80-i);

            deck.get(i).setPreserveRatio(true);

            deck.get(i).setVisible(true);
        }
        iv[3] = new ImageView((g.players.get(g.currentPlayer).getHand().get(0).getImage(true)));
        iv[3].setX(180);
        iv[3].setY(60);
        iv[3].setFitWidth(120);
        iv[3].setFitHeight(200);
        iv[3].setPreserveRatio(true);
        iv[3].setVisible(false);
        iv[0] = new ImageView((g.players.get(g.currentPlayer).getHand().get(0).getImage(true)));
        iv[0].setX(30);
        iv[0].setY(250);
        iv[0].setFitHeight(200);
        iv[0].setFitWidth(120);
        iv[0].setPreserveRatio(true);
        iv[0].setOnMouseClicked(eh1);
        iv[1] = new ImageView((g.players.get(g.currentPlayer).getHand().get(1).getImage(true)));
        iv[1].setX(180);
        iv[1].setY(250);
        iv[1].setFitHeight(200);
        iv[1].setFitWidth(120);
        iv[1].setPreserveRatio(true);
        iv[1].setOnMouseClicked(eh2);
        iv[2] = new ImageView((g.players.get(g.currentPlayer).getHand().get(2).getImage(true)));
        iv[2].setX(330);
        iv[2].setY(250);
        iv[2].setFitHeight(200);
        iv[2].setFitWidth(120);
        iv[2].setPreserveRatio(true);
        iv[2].setOnMouseClicked(eh3);
        for (int j = 0; j < 2; j++) {
            System.out.println("Player Number: " + j);
            System.out.println("");
            for (int i = 0; i < g.players.get(j).getHand().size(); i++) {
                System.out.println(g.players.get(j).getHand().get(i));
            }
            System.out.println("");}
           /* Scanner n = new Scanner(System.in);
            System.out.println("Current Player:" + g.currentPlayer);
            int choice = n.nextInt();
            g.playTurn(g.players.get(g.currentPlayer).getHand().get(choice));
            System.out.println("");

            System.out.println(g.players.get(0).Score());
            System.out.println(g.players.get(1).Score()); */
        Group root = new Group(rects[0], rects[1], rects[2], score1, score2, suit, iv[0],iv[1],iv[2],iv[3], currrentPlayer);
        for (int i = 0; i < g.deck.size(); i++) {
            root.getChildren().add(deck.get(i));
        }
        Scene scene = new Scene(root, Color.GREEN);
        stage.setTitle("Briscola");
        stage.setScene(scene);
        stage.setHeight(500);
        stage.setWidth(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    private void gameOver(Stage stage){
        Button b = new Button("Play another match");
        Button b2 = new Button("Exit");

        Group root2 = new Group();
        root2.getChildren().add(b);

        Text gameOver = new Text("Game over");
        gameOver.setX(200);
        gameOver.setY(100);

        root2.getChildren().add(gameOver);
        Text whoWon = null;
        if(!g.isTeamGame){
            int whichPlayerWon = 1;

            if(g.players.get(0).Score()<g.players.get(1).Score()){
                whichPlayerWon=2;
                whoWon= new Text("The winning player is: " + whichPlayerWon);

            }
        }
        else{
            int whichPlayerWon = 1;

            if(g.getScoreForTeam(1)>g.getScoreForTeam(0)){
                whichPlayerWon=2;
            }
            whoWon = new Text("The winning team is: " + whichPlayerWon);
        }
        whoWon.setY(50);
        b2.setLayoutY(60);

        root2.getChildren().add(whoWon);
        root2.getChildren().add(b2);


        Scene secondScene = new Scene(root2, 540, 280);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);
        EventHandler<ActionEvent> event = new EventHandler<>() {
            public void handle(ActionEvent e)
            {
                try {
                    newWindow.close();
                    restart(stage);
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        };
        EventHandler<ActionEvent> event2 = new EventHandler<>() {
            public void handle(ActionEvent e)
            {
                newWindow.close();
                cleanup(stage);
            }
        };
        b2.setOnAction(event2);

        b.setOnAction(event);






        // Set position of second window, related to primary window.
        newWindow.setX(0);
        newWindow.setY(0);
        newWindow.show();

    }
    public void restart (Stage stage) throws MalformedURLException, FileNotFoundException {
        cleanup(stage);
        stage = new Stage();
        initGame(stage);
    }
    public void cleanup(Stage stage){
        stage.hide();
        g = null;
        iv = new ImageView[5];
        deck.clear();
        EventHandler<javafx.scene.input.MouseEvent> eh1;
        EventHandler<javafx.scene.input.MouseEvent> eh2;
        EventHandler<javafx.scene.input.MouseEvent> eh3;
        Rectangle[] rects = {new Rectangle(30, 250, 120, 200),new Rectangle(180, 250, 120, 200),new Rectangle(330, 250, 120, 200)};
        Text score1= new Text();
        Text score2= new Text();
        Text currrentPlayer = new Text("Current player: ");

    }
    public void drawAnimation(int whichPlayer){
        for (int i = 0; i < 3; i++) {
            if(!iv[i].isVisible()){
                TranslateTransition animation = new TranslateTransition();
                animation.setByX((deck.get(deck.size()-1).getX()-iv[i].getX()));
                animation.setByY((deck.get(deck.size()-1).getY()-iv[i].getY()));

                animation.setDuration(Duration.millis(1000));

            }
        }
    }
}