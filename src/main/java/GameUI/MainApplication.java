package GameUI;

import Game.Cards.Card;
import Game.GameSession;
import Game.Player;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainApplication extends Application {
    private GameSession g = null;
    private ImageView[][] iv = new ImageView[7][3];

    private ArrayList<ImageView> deck = new ArrayList<>();
    private Stage stage = new Stage();
    private EventHandler<javafx.scene.input.MouseEvent> eh1;
    private EventHandler<javafx.scene.input.MouseEvent> eh2;
    private EventHandler<javafx.scene.input.MouseEvent> eh3;
    private Rectangle[] rects = { new Rectangle(30, 250, 120, 200), new Rectangle(180, 250, 120, 200),
            new Rectangle(330, 250, 120, 200) };
    private Text score1 = new Text();
    private Text score2 = new Text();
    private Text currrentPlayer = new Text("Current player: ");
    private int ammountOfPLayers = 2;
    public static MainApplication gameUI;
    private int player = 0;

    private CardTextureStore.CardSkin currentSkin = CardTextureStore.CardSkin.CardBack1;

    /**
     * Start function it defines ui and takes care of displaying menu etc
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        gameUI = this;
        Button play_match = new Button("Play match");
        Button exit = new Button("Exit");

        Group menuRoot = new Group();
        menuRoot.getChildren().add(play_match);

        Text title = new Text(250, 200, "Briscola");
        title.setFill(Color.YELLOW);
        title.setStrokeWidth(2);
        title.setStroke(Color.BLACK);
        title.setFont(Font.font("verdana", 100));
        menuRoot.getChildren().add(title);

        Text subtitle = new Text(260, 230, "by Group 13: The Briscola Bois");
        subtitle.setFont(Font.font("verdana", FontWeight.BOLD, 10));
        menuRoot.getChildren().add(subtitle);

        exit.setPrefSize(200, 50);
        exit.setLayoutY(400); // 150
        exit.setLayoutX(650); // 170
        play_match.setPrefSize(200, 50);
        play_match.setLayoutY(400); // 50
        play_match.setLayoutX(50); // 170
        Button bMenu = new Button("Settings");
        bMenu.setPrefSize(200, 50);
        bMenu.setLayoutY(400); // 100
        bMenu.setLayoutX(350); // 170
        menuRoot.getChildren().add(bMenu);

        menuRoot.getChildren().add(exit);
        Color background = Color.rgb(60, 124, 83);
        Scene MainScene = new Scene(menuRoot, 900, 1000, background); // w:540, h:280

        Stage menuWindow = new Stage();
        menuWindow.setTitle("Main Menu");
        menuWindow.setScene(MainScene);
        // Starts the game from menu
        EventHandler<ActionEvent> startGameFromMenu = new EventHandler<>() {
            public void handle(ActionEvent e) {
                try {
                    menuWindow.close();
                    restart(stage);
                } catch (MalformedURLException | FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        };
        // Exits the menu and closes game
        EventHandler<ActionEvent> exit_event = new EventHandler<>() {
            public void handle(ActionEvent e) {
                menuWindow.close();
                cleanup(stage);
            }
        };
        exit.setOnAction(exit_event);

        play_match.setOnAction(startGameFromMenu);

        // Opens settings
        EventHandler<ActionEvent> settingsEvent = new EventHandler<>() {
            public void handle(ActionEvent e) {
                ComboBox<String> howManyPlayers = new ComboBox<String>();

                ComboBox<CardTextureStore.CardSkin> skin = new ComboBox<CardTextureStore.CardSkin>();
                skin.setPromptText("Choose the skin");
                skin.getItems().add(CardTextureStore.CardSkin.CardBack1);
                skin.getItems().add(CardTextureStore.CardSkin.Pizza);

                howManyPlayers.setPromptText("Choose the amount of players");
                howManyPlayers.getItems().add("2");
                howManyPlayers.getItems().add("4");
                howManyPlayers.getItems().add("6");

                Button save_and_exit = new Button("Save and exit");

                Group settingsRoot = new Group();
                settingsRoot.getChildren().add(howManyPlayers);
                skin.setPrefSize(240, 50);
                skin.setLayoutY(150);
                skin.setLayoutX(150);
                howManyPlayers.setPrefSize(240, 50);
                howManyPlayers.setLayoutY(100);
                howManyPlayers.setLayoutX(150);
                save_and_exit.setPrefSize(240, 50);
                save_and_exit.setLayoutY(50);
                save_and_exit.setLayoutX(150);

                settingsRoot.getChildren().add(skin);
                settingsRoot.getChildren().add(save_and_exit);

                Color background = Color.rgb(60, 124, 83);

                Scene settingScene = new Scene(settingsRoot, 540, 280, background);

                // New window (Stage)
                Stage menu = new Stage();
                menu.setScene(settingScene);

                menu.setTitle("Setting");
                menu.setScene(settingScene);
                // Closes the settings
                EventHandler<ActionEvent> event2 = new EventHandler<>() {
                    public void handle(ActionEvent e) {
                        menu.close();

                    }
                };
                // Allows the choice of the ammount of players
                EventHandler<ActionEvent> howManyPlayersEvent = new EventHandler<>() {
                    public void handle(ActionEvent e) {
                        ammountOfPLayers = Integer.parseInt(howManyPlayers.getValue());
                    }
                };
                // Allows card reverse choice
                EventHandler<ActionEvent> skinChoice = new EventHandler<>() {
                    public void handle(ActionEvent e) {
                        currentSkin = skin.getValue();
                    }
                };
                skin.setOnAction(skinChoice);
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

    }

    public void handlers(int whichCard) {
        player = g.currentPlayer;
        iv[6][0].setImage(CardTextureStore.getFrontTexture(g.players[player].getHand().get(whichCard)));
        iv[6][0].setVisible(true);

        g.playTurn(g.players[player].getHand().get(whichCard));

        for (int i = 0; i < 3; i++) {
            try {
                iv[player][i].setImage(CardTextureStore.getBackTexture(currentSkin));
                iv[player][i].setVisible(true);
            } catch ( IndexOutOfBoundsException ignored) {
                iv[player][i].setVisible(false);
                rects[i].setVisible(false);
            }
        }

        for (int i = 0; i < 3; i++) {
            try {
                iv[g.currentPlayer][i].setImage(CardTextureStore.getFrontTexture(g.players[g.currentPlayer].getHand().get(i)));
                iv[g.currentPlayer][i].setVisible(true);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        for (int i = g.deck.size() + 2; i < deck.size(); i++) {
            if (g.deck.isEmpty()) {
                deck.get(i).setVisible(false);
            }

            if (!deck.get(i).isVisible()) {
                break;
            }
            deck.get(i).setVisible(false);

        }
        iv[6][0].setVisible(!g.Table.isEmpty());
        boolean isGameOver = true;
        for (int i = 0; i < ammountOfPLayers; i++) {
            if (!g.getPlayer(i).getHand().isEmpty()) {
                isGameOver = true;
                break;
            }
            isGameOver = false;

        }
        if (!isGameOver) {
            gameOver(stage);
        }

        currrentPlayer.setText("Current player: " + (g.currentPlayer + 1));
        if (!g.isTeamGame) {
            score1.setText("Player 1: " + g.players[0].Score());
            score2.setText("Player 2: " + g.players[1].Score());
        } else {
            score1.setText("Team 1: " + g.getScoreForTeam(0));
            score2.setText("Team 2: " + g.getScoreForTeam(1));

        }
    }

    /**
     * Starts the main game
     *
     * @param stage
     * @throws MalformedURLException if url is bad
     * @throws FileNotFoundException if card tried to display doesn't exist
     */
    private void initGame(Stage stage) throws MalformedURLException, FileNotFoundException {
        // Here are a lot of handlers for every players card on hand

        EventHandler<javafx.scene.input.MouseEvent> playFirstCardFirstPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 0) {
                    handlers(0);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playSecondCardFirstPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 0) {
                    handlers(1);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playThirdCardFirstPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 0) {
                    handlers(2);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playFirstCardSecondPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 1) {
                    handlers(0);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playSecondCardSecondPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 1) {
                    handlers(1);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playThirdCardSecondPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 1) {
                    handlers(2);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playFirstCardThirdPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 2) {
                    handlers(0);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playSecondCardThirdPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 2) {
                    handlers(1);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playThirdCardThirdPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 2) {
                    handlers(2);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playFirstCardSixthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 5) {
                    handlers(0);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playSecondCardSixthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 5) {
                    handlers(1);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playThirdCardSixthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 5) {
                    handlers(2);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playFirstCardFourthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 3) {
                    handlers(0);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playSecondCardFourthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 3) {
                    handlers(1);
                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playThirdCardFourthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 3) {
                    handlers(2);

                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playFirstCardFifthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 4) {
                    handlers(0);

                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playSecondCardFifthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 4) {
                    handlers(1);

                }
            }
        };
        EventHandler<javafx.scene.input.MouseEvent> playThirdCardFifthPlayer = new EventHandler<javafx.scene.input.MouseEvent>() {

            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (g.currentPlayer == 4) {
                    handlers(2);

                }
            }
        };

        // Create new game session and init texts

        // ALL HUMANS FOR NOW
        Player[] players = new Player[ammountOfPLayers];
        for (int i = 0; i < ammountOfPLayers; ++i)
            players[i] = new Player();

        g = new GameSession(players);

        g.addNextPlayerCallback(()->{
            try{
                resetVisibility();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        });

        score1 = new Text(20, 20, "Player 1: " + g.players[0].Score());
        score1.setFill(Color.WHITE);
        score1.setStrokeWidth(1);
        score1.setStroke(Color.BLACK);
        score1.setFont(Font.font("verdana", FontWeight.BOLD, 13));
        score2 = new Text(990, 20, "Player 2: " + g.players[1].Score());
        score2.setFill(Color.WHITE);
        score2.setStrokeWidth(1);
        score2.setStroke(Color.BLACK);
        score2.setFont(Font.font("verdana", FontWeight.BOLD, 13));
        currrentPlayer = new Text(505, 20, "Current player: " + (g.currentPlayer + 1));
        currrentPlayer.setFill(Color.WHITE);
        currrentPlayer.setStrokeWidth(1);
        currrentPlayer.setStroke(Color.BLACK);
        currrentPlayer.setFont(Font.font("verdana", FontWeight.BOLD, 13));
        g.startRound();
        // while (true) {
        Card.Number cardNumber = g.deck.getBriscola().number;
        Card.Suit briscsuit = g.deck.getBriscola().suit;
        System.out.println(briscsuit + " " + cardNumber);
        String userHome = System.getProperty("user.dir");
        /*
         * Depricated method
         * Image imageForFile = new Image(userHome + "../PNG-cards-1.3/" + cardNumber +
         * "_of_" + briscsuit + ".png");
         */
        Text suit = new Text(750, 510, "Briscola: " + g.deck.getBriscola().suit);
        // adds card to UI deck display
        deck.add(0, new ImageView(CardTextureStore.getFrontTexture(g.deck.get(0))));
        deck.get(0).setFitWidth(90);
        deck.get(0).setFitHeight(150);
        deck.get(0).setX(751);
        deck.get(0).setY(520);

        deck.get(0).setPreserveRatio(true);

        deck.get(0).setVisible(true);
        // Fills the ui deck to be displayed
        for (int i = 1; i < g.deck.size(); i++) {

            deck.add(i, new ImageView(CardTextureStore.getBackTexture(currentSkin)));
            deck.get(i).setFitWidth(90);
            deck.get(i).setFitHeight(150);

            deck.get(i).setX(580 + i);
            deck.get(i).setY(260 - i);

            deck.get(i).setPreserveRatio(true);

            deck.get(i).setVisible(true);
        }
        // Sets location of all the players cards and adds event handlers

        var backTexture = CardTextureStore.getBackTexture(currentSkin);

        iv[6][0] = new ImageView(backTexture);
        iv[6][0].setX(440);
        iv[6][0].setY(260);
        iv[6][0].setFitWidth(90);
        iv[6][0].setFitHeight(150);
        iv[6][0].setPreserveRatio(true);
        iv[6][0].setVisible(false);
        iv[0][0] = new ImageView(CardTextureStore.getFrontTexture(g.players[g.currentPlayer].getHand().get(0)));
        iv[0][0].setX(400);
        iv[0][0].setY(450);
        iv[0][0].setFitHeight(150);
        iv[0][0].setFitWidth(90);
        iv[0][0].setPreserveRatio(true);
        iv[0][1] = new ImageView(CardTextureStore.getFrontTexture(g.players[g.currentPlayer].getHand().get(1)));
        iv[0][1].setX(500);
        iv[0][1].setY(450);
        iv[0][1].setFitHeight(150);
        iv[0][1].setFitWidth(90);
        iv[0][1].setPreserveRatio(true);
        iv[0][2] = new ImageView(CardTextureStore.getFrontTexture(g.players[g.currentPlayer].getHand().get(2)));
        iv[0][2].setX(600);
        iv[0][2].setY(450);
        iv[0][2].setFitHeight(150);
        iv[0][2].setFitWidth(90);
        iv[0][2].setPreserveRatio(true);
        iv[1][0] = new ImageView(backTexture);
        iv[1][0].setY(50);
        iv[1][0].setRotate(180);
        iv[1][0].setFitWidth(90);
        iv[1][0].setFitHeight(150);
        iv[1][1] = new ImageView(backTexture);
        iv[1][1].setY(50);
        iv[1][1].setRotate(180);
        iv[1][1].setFitWidth(90);
        iv[1][1].setFitHeight(150);
        iv[1][2] = new ImageView(backTexture);
        iv[1][2].setY(50);
        iv[1][2].setRotate(180);
        iv[1][2].setFitWidth(90);
        iv[1][2].setFitHeight(150);
        iv[1][0] = new ImageView(backTexture);
        iv[1][0].setX(80);
        iv[1][0].setY(50);
        iv[1][0].setRotate(-180);
        iv[1][0].setFitHeight(150);
        iv[1][0].setFitWidth(90);
        iv[1][0].setPreserveRatio(true);
        iv[1][0].setVisible(true);
        iv[2][0] = new ImageView(backTexture);
        iv[2][0].setX(80);
        iv[2][0].setY(150);
        iv[2][0].setRotate(-90);
        iv[2][0].setFitHeight(150);
        iv[2][0].setFitWidth(90);
        iv[2][0].setPreserveRatio(true);
        iv[2][0].setVisible(false);
        iv[2][1] = new ImageView(backTexture);
        iv[2][1].setX(80);
        iv[2][1].setY(250);
        iv[2][1].setRotate(-90);
        iv[2][1].setFitHeight(150);
        iv[2][1].setFitWidth(90);
        iv[2][1].setPreserveRatio(true);
        iv[2][1].setVisible(false);
        iv[2][2] = new ImageView(backTexture);
        iv[2][2].setX(80);
        iv[2][2].setY(350);
        iv[2][2].setRotate(-90);
        iv[2][2].setFitHeight(150);
        iv[2][2].setFitWidth(90);
        iv[2][2].setPreserveRatio(true);
        iv[2][2].setVisible(false);
        iv[3][0] = new ImageView(backTexture);
        iv[3][0].setX(920);
        iv[3][0].setY(150);
        iv[3][0].setRotate(90);
        iv[3][0].setFitHeight(150);
        iv[3][0].setFitWidth(90);
        iv[3][0].setPreserveRatio(true);
        iv[3][0].setVisible(false);
        iv[3][1] = new ImageView(backTexture);
        iv[3][1].setX(920);
        iv[3][1].setY(250);
        iv[3][1].setRotate(90);
        iv[3][1].setFitHeight(150);
        iv[3][1].setFitWidth(90);
        iv[3][1].setPreserveRatio(true);
        iv[3][1].setVisible(false);
        iv[3][2] = new ImageView(backTexture);
        iv[3][2].setX(920);
        iv[3][2].setY(350);
        iv[3][2].setRotate(90);
        iv[3][2].setFitHeight(150);
        iv[3][2].setFitWidth(90);
        iv[3][2].setPreserveRatio(true);
        iv[3][2].setVisible(false);
        iv[4][0] = new ImageView(backTexture);
        iv[4][0].setX(80);
        iv[4][0].setY(350);
        iv[4][0].setRotate(-110);
        iv[4][0].setFitHeight(150);
        iv[4][0].setFitWidth(90);
        iv[4][0].setPreserveRatio(true);
        iv[4][0].setVisible(false);
        iv[4][1] = new ImageView(backTexture);
        iv[4][1].setX(80);
        iv[4][1].setY(400);
        iv[4][1].setRotate(-90);
        iv[4][1].setFitHeight(150);
        iv[4][1].setFitWidth(90);
        iv[4][1].setPreserveRatio(true);
        iv[4][1].setVisible(false);
        iv[4][2] = new ImageView(backTexture);
        iv[4][2].setX(80);
        iv[4][2].setY(450);
        iv[4][2].setRotate(-70);
        iv[4][2].setFitHeight(150);
        iv[4][2].setFitWidth(90);
        iv[4][2].setPreserveRatio(true);
        iv[4][2].setVisible(false);
        iv[5][0] = new ImageView(backTexture);
        iv[5][0].setX(920);
        iv[5][0].setY(350);
        iv[5][0].setRotate(110);
        iv[5][0].setFitHeight(150);
        iv[5][0].setFitWidth(90);
        iv[5][0].setPreserveRatio(true);
        iv[5][0].setVisible(false);
        iv[5][1] = new ImageView(backTexture);
        iv[5][1].setX(920);
        iv[5][1].setY(400);
        iv[5][1].setRotate(90);
        iv[5][1].setFitHeight(150);
        iv[5][1].setFitWidth(90);
        iv[5][1].setPreserveRatio(true);
        iv[5][1].setVisible(false);
        iv[5][2] = new ImageView(backTexture);
        iv[5][2].setX(920);
        iv[5][2].setY(450);
        iv[5][2].setRotate(70);
        iv[5][2].setFitHeight(150);
        iv[5][2].setFitWidth(90);
        iv[5][2].setPreserveRatio(true);
        iv[5][2].setVisible(false);
        iv[1][0].setX(400);
        iv[1][0].setPreserveRatio(true);
        iv[1][1].setX(500);
        iv[1][1].setPreserveRatio(true);

        iv[1][0].setOnMouseClicked(playFirstCardSecondPlayer);
        iv[1][1].setOnMouseClicked(playSecondCardSecondPlayer);
        iv[1][2].setOnMouseClicked(playThirdCardSecondPlayer);

        iv[0][0].setOnMouseClicked(playFirstCardFirstPlayer);
        iv[0][1].setOnMouseClicked(playSecondCardFirstPlayer);
        iv[0][2].setOnMouseClicked(playThirdCardFirstPlayer);

        iv[2][0].setOnMouseClicked(playFirstCardThirdPlayer);
        iv[2][1].setOnMouseClicked(playSecondCardThirdPlayer);
        iv[2][2].setOnMouseClicked(playThirdCardThirdPlayer);

        iv[3][0].setOnMouseClicked(playFirstCardFourthPlayer);
        iv[3][1].setOnMouseClicked(playSecondCardFourthPlayer);
        iv[3][2].setOnMouseClicked(playThirdCardFourthPlayer);

        iv[4][0].setOnMouseClicked(playFirstCardFifthPlayer);
        iv[4][1].setOnMouseClicked(playSecondCardFifthPlayer);
        iv[4][2].setOnMouseClicked(playThirdCardFifthPlayer);

        iv[5][0].setOnMouseClicked(playFirstCardSixthPlayer);
        iv[5][1].setOnMouseClicked(playSecondCardSixthPlayer);
        iv[5][2].setOnMouseClicked(playThirdCardSixthPlayer);
        iv[1][2].setX(600);
        iv[1][2].setPreserveRatio(true);
        for (int i = 0; i < iv[0].length; i++) {
            if (ammountOfPLayers == 4) {
                iv[2][i].setVisible(true);
                iv[3][i].setVisible(true);
            }
            if (ammountOfPLayers == 6) {
                iv[2][2].setRotate(-70);
                iv[2][2].setY(150);
                iv[2][1].setY(100);
                iv[2][0].setRotate(-110);
                iv[2][0].setY(50);
                iv[3][2].setRotate(70);
                iv[3][2].setY(150);
                iv[3][1].setY(100);
                iv[3][0].setRotate(110);
                iv[3][0].setY(50);
                iv[2][i].setVisible(true);
                iv[3][i].setVisible(true);
                iv[4][i].setVisible(true);
                iv[5][i].setVisible(true);
            }
        }
        for (int j = 0; j < 2; j++) {
            System.out.println("Player Number: " + j);
            System.out.println("");
            for (int i = 0; i < g.players[j].getHand().size(); i++) {
                System.out.println(g.players[j].getHand().get(i));
            }
            System.out.println("");
        }
        /*
         * Scanner n = new Scanner(System.in);
         * System.out.println("Current Player:" + g.currentPlayer);
         * int choice = n.nextInt();
         * g.playTurn(g.players[g.currentPlayer].getHand().get(choice));
         * System.out.println("");
         *
         * System.out.println(g.players.get(0).Score());
         * System.out.println(g.players.get(1).Score());
         */
        Group root = new Group(score1, score2, suit, iv[6][0], currrentPlayer);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 6; j++) {
                root.getChildren().add(iv[j][i]);
            }
        }
        for (int i = 0; i < g.deck.size(); i++) {
            root.getChildren().add(deck.get(i));
        }
        Scene scene = new Scene(root, Color.GREEN);
        stage.setTitle("Briscola");
        stage.setScene(scene);
        stage.setHeight(700);
        stage.setWidth(1100);
        stage.show();
    }

    /**
     * main method used to run the app
     *
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }

    private void gameOver(Stage stage) {
        Button b = new Button("Play another match");
        Button b2 = new Button("Exit");

        Group root2 = new Group();

        b.setLayoutX(170);
        b.setLayoutY(160);

        root2.getChildren().add(b);
        int whichPlayerWon = 1;
        if (g.players[0].Score() < g.players[1].Score()) {
            whichPlayerWon = 2;
        }
        Text gameOver = new Text("Game over");
        gameOver.setX(120);
        gameOver.setY(100);
        gameOver.setFill(Color.WHITE);
        gameOver.setStrokeWidth(2);
        gameOver.setStroke(Color.BLACK);
        gameOver.setFont(Font.font("verdana", 50));

        root2.getChildren().add(gameOver);

        Text whoWon = new Text("The winning player is: " + whichPlayerWon);
        whoWon.setFont(Font.font("verdana", FontWeight.BOLD, 10));
        whoWon.setX(160);
        whoWon.setY(120);

        Text score = new Text("Score: " + g.players[whichPlayerWon - 1].Score());
        score.setX(165);
        score.setY(140);
        b2.setLayoutX(205);
        b2.setLayoutY(200);

        root2.getChildren().add(whoWon);
        root2.getChildren().add(score);
        root2.getChildren().add(b2);

        Color background = Color.rgb(60, 124, 83);

        Scene secondScene = new Scene(root2, 540, 280, background);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("Second Stage");
        newWindow.setScene(secondScene);
        EventHandler<ActionEvent> event = new EventHandler<>() {
            public void handle(ActionEvent e) {
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
            public void handle(ActionEvent e) {
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

    /**
     * This method is used when we want to restart the game
     *
     * @param stage
     * @throws MalformedURLException
     * @throws FileNotFoundException
     */
    public void restart(Stage stage) throws MalformedURLException, FileNotFoundException {
        cleanup(stage);
        stage = new Stage();
        initGame(stage);
    }

    /**
     * This method is used when restarting the game to reset all the parameters to
     * original form
     *
     * @param stage
     */
    public void cleanup(Stage stage) {
        stage.hide();
        g = null;
        iv = new ImageView[7][3];
        deck.clear();

    }

    public void drawAnimation(int whichPlayer) {
        for (int i = 0; i < 3; i++) {
            if (!iv[whichPlayer][i].isVisible()) {
                TranslateTransition animation = new TranslateTransition();
                animation.setByX((deck.get(deck.size() - 1).getX() - iv[g.currentPlayer][i].getX()));
                animation.setByY((deck.get(deck.size() - 1).getY() - iv[g.currentPlayer][i].getY()));

                animation.setDuration(Duration.millis(1000));

                animation.play();

            }
        }
    }

    public void resetVisibility() {
        int currentPlayer = g.currentPlayer;
        for (int j = 0; j < ammountOfPLayers; j++) {
            for (int i = 0; i < 3; i++) {
                if (g.getPlayer(j).getHand().size() <= i) {
                    iv[j][i].setVisible(false);
                    continue;
                }
                iv[j][i].setVisible(true);
                if (j != currentPlayer) {
                    iv[j][i].setImage(CardTextureStore.getBackTexture(currentSkin));
                } else {
                    iv[j][i].setImage(CardTextureStore.getFrontTexture(g.players[j].getHand().get(i)));
                }
            }
        }
    }
}
