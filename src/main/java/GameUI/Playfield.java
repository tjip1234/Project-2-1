package GameUI;

import Game.Bots.GreedyBot;
import Game.Cards.Card;
import Game.GameSession;
import Game.Player;
import GameUI.PlayfieldComponents.DrawableCard;
import GameUI.PlayfieldComponents.Easings;
import GameUI.PlayfieldComponents.PlayerCardHandler;
import GameUI.PlayfieldComponents.Transforms.TransformHandler;
import GameUI.Utils.MathUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.security.Key;
import java.util.ArrayList;
import java.util.Stack;
import java.util.function.Consumer;

public class Playfield extends Stage {

    private final TransformHandler transformHandler = new TransformHandler();

    public int cardNumber = 0;

    public final GameSession game;

    public final ArrayList<DrawableCard> tableCards = new ArrayList<>();

    public final Stack<DrawableCard> deckCards = new Stack<>();

    private final Group playfieldElements;

    public final Text[] scoreTexts;


    public Playfield(Stage menuStage) {
        setOnCloseRequest(e -> {
            menuStage.show();
            this.close();
        });

        setTitle("Briscola - Game");
        transformHandler.start();

        playfieldElements = new Group();
        playfieldElements.setAutoSizeChildren(false);
        playfieldElements.resize(700,700);

        Player[] players = new Player[BriscolaConfigs.getPlayerNumber()];
        scoreTexts = new Text[players.length];

        for(int i = 0; i < players.length; ++i){
            players[i] = new GreedyBot();
            scoreTexts[i] = new Text(0, 20+ 20 * i, String.format("Player %d: 0", i+1));
            scoreTexts[i].setFont(Font.font("verdana", FontWeight.BOLD, 20));
            playfieldElements.getChildren().add(scoreTexts[i]);
        }

        game = new GameSession(players);
        game.setBotVisualHook(this::onBotMove);
        game.addNextPlayerCallback(this::onNextPlayer);
        game.addNextTrickCallback(this::onTrickComplete);

        createCards(playfieldElements);
        createPlayerCardHandlers();

        Scene scene = new Scene(playfieldElements, 700, 700, Color.GREEN);
        setScene(scene);

        setResizable(false);
        show();

        game.startRound();

        // Show the briscola
        deckCards.get(0).setCard(game.deck.getBriscola());
        deckCards.get(0).flipToFront();
        deckCards.get(0).rotateTo(System.nanoTime(),-20);
        deckCards.get(0).setX(110);

        spreadCards();
        onNextPlayer(-1, 0);

        scene.setOnKeyPressed(this::onKeyDown);
    }

    private void createCards(Group playfieldElements) {
        double cardOffsetX = 0;
        for (int i = 0; i < game.deck.size(); ++i) {
            var card = createDrawableCard();
            double deckPosX = 100;
            card.setX(deckPosX + cardOffsetX);
            double deckPosY = 0;
            card.setY(deckPosY);

            playfieldElements.getChildren().add(card);
            deckCards.push(card);
            cardOffsetX += 0.2;
        }
    }

    private DrawableCard createDrawableCard() {
        var drawableCard = new DrawableCard();
        drawableCard.setLayoutX(350 - drawableCard.getFitWidth() / 2);
        drawableCard.setLayoutY(350 - drawableCard.getFitHeight() / 2);
        return drawableCard;
    }

    private PlayerCardHandler[] cardHandlers;
    private void createPlayerCardHandlers(){
        cardHandlers  = new PlayerCardHandler[game.players.length];
        float angleDelta = 360.0f / game.players.length;

        for(int i = 0; i < game.players.length; ++i){
            cardHandlers[i] = new PlayerCardHandler(this, angleDelta * i, i);
        }
    }

    private void spreadCards(){
        var startTime = System.nanoTime();

        for (int i = 0; i < cardHandlers.length; ++i)
            for (Card card : game.players[i].getHand()) {
                var cardDrawable = deckCards.pop();
                cardDrawable.setCard(card);
                cardHandlers[i].addCardToHand(cardDrawable, startTime);
                startTime += 200 * 1000000;
            }
    }

    public void putCardOnTable(DrawableCard card){
        card.setViewOrder(--cardNumber);
        card.moveTo(System.nanoTime(), 0,0);

        tableCards.add(card);
    }

    private void onNextPlayer(Integer prevPlayer, Integer newPlayer){
        if(prevPlayer.equals(newPlayer)){
            cardHandlers[prevPlayer].addNewCard(true);
            return;
        }

        if(prevPlayer != -1) {
            cardHandlers[prevPlayer].flip();
            cardHandlers[prevPlayer].addNewCard(false);
        }

        cardHandlers[newPlayer].flip();
    }

    private void onTrickComplete(Integer winningPlayer){
        scoreTexts[winningPlayer].setText(String.format("Player %d: %d",winningPlayer + 1,  game.players[winningPlayer].Score()));

        var winningDirection = MathUtils.getCircularPosition(500, (360.0f / game.players.length) * winningPlayer);

        for(var card : tableCards)
            card.scheduleTransform(System.nanoTime() + 1000 * 1000000, f -> card.moveTo(System.nanoTime(), winningDirection.x(), winningDirection.y()));

        tableCards.clear();
    }

    private void onBotMove(int playerNumber, Card card){
        cardHandlers[playerNumber].botPlayCard(card);
    }

    private void onKeyDown(KeyEvent e){
        if(!e.getCode().equals(KeyCode.SPACE))
            return;

        game.botPlayTurn();
    }
}