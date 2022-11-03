package Game;

import Cards.Card;
import Cards.Deck;
import Game.Bots.Bot;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GameSession {
    public Player[] players;
    public ArrayList<Card> Table = new ArrayList<>();
    public int currentPlayer;
    int startPlayer = 0;

    private final ArrayList<Runnable> onNextPlayer = new ArrayList<>();

    private final HashSet<Card> playedCards = new HashSet<>();

    public final boolean isTeamGame;

    public Deck deck;

    public GameSession(Player... players) {
        if (players.length == 0)
            throw new UnsupportedOperationException("Games don't work like that. Dumbss.");
        if (players.length == 1)
            throw new UnsupportedOperationException(
                    "Trying to play this game by yourself? How sad... Go get some friends");

        if (players.length == 5)
            throw new UnsupportedOperationException("Briscola Chiamata is unsupported.");

        if (players.length > 6)
            throw new UnsupportedOperationException(
                    "As fun as that may sound, you can't play briscola with that many players. The limit is 6.");

        currentPlayer = 0;

        this.players = players;

        isTeamGame = players.length != 2 && players.length % 2 == 0;

        if (players.length == 3)
            deck = new Deck(39);
        else if (players.length == 6)
            deck = new Deck(36);
        else
            deck = new Deck(40);
    }

    public void addNextPlayerCallback(Runnable callback){
        if(callback == null)
            return;
        onNextPlayer.add(callback);
    }

    private void executeNextPlayerCallback(){
        for (var runnable : onNextPlayer){
            runnable.run();;
        }
    }

    public void startRound() {
        deck.reshuffle();
        playedCards.clear();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < players.length; i++) {
                players[(i + startPlayer) % players.length].addHand(deck.pop());
            }
        }
    }

    public void playTurn(Card card){
        players[currentPlayer].removeHand(card);
        Table.add(card);
        playedCards.add(card);

        currentPlayer = (currentPlayer + 1) % players.length;

        if (currentPlayer == startPlayer) {
            currentPlayer = startPlayer = determineCycleWinner();
            while (!Table.isEmpty()) {
                players[startPlayer].addCollectedCard(Table.remove(0));
            }
            if (!deck.isEmpty()) {
                for (int i = 0; i < players.length; i++) {
                    players[(i + startPlayer) % players.length].addHand(deck.pop());
                }
            }
            executeNextPlayerCallback();
            if (gameOver()) {
                int playerWinner = 0;
                for (int i = 1; i < players.length; i++) {
                    if (players[i].Score() > players[playerWinner].Score())
                        playerWinner = i;
                }
            }
        }
    }

    private int determineCycleWinner() {
        int winner = 0;

        for (int i = 0; i < Table.size(); ++i)
            if (Table.get(i).compareTo(Table.get(winner), deck.getBriscola().suit) >= 1)
                winner = i;

        return (winner + startPlayer) % players.length;
    }

    public boolean gameOver() {
        return players[currentPlayer].getHand().isEmpty();
    }

    public Integer getScoreForTeam(int teamNumber) {
        if (!isTeamGame)
            return 0;

        int total = 0;

        for (int i = teamNumber; i < players.length; i += 2)
            total += players[i].Score();

        return total;
    }

    public Player getPlayer(int playerNumber) {
        return players[playerNumber % players.length];
    }

    public int getTeamNumber(int playerNumber) {
        return playerNumber % 2;
    }

    public boolean isBot(int playerNumber) {
        return (players[playerNumber] instanceof Bot);
    }

    public boolean isCurrentPlayerABot() {
        return players[currentPlayer] instanceof Bot;
    }

    public void botPlayTurn() {
        if (!(players[currentPlayer] instanceof Bot playerBot))
            return;

        try {
            playerBot.playedCards = Collections.unmodifiableSet(playedCards);
            Card playedCard;
            playTurn(playedCard=playerBot.MakeDecision(Collections.unmodifiableList(Table), (deck.getBriscola().suit)));
            System.out.printf("Bot played %s\n", playedCard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
