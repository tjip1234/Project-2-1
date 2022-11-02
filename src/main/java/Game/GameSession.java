package Game;

import Cards.Card;
import Cards.Deck;
import com.example.project_2.HelloApplication;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class GameSession {
    public Player[] players;
    public ArrayList<Card> Table = new ArrayList<>();
    public int currentPlayer;
    int startPlayer = 0;

    public final boolean isTeamGame;

    public Deck deck;

    public GameSession(Player... players) {
        if (players.length <= 0)
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

    public void startRound() {
        deck.reshuffle();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < players.length; i++) {
                players[(i + startPlayer) % players.length].addHand(deck.pop(), (i + startPlayer) % players.length);
            }
        }
    }

    public void playTurn(Card card) throws MalformedURLException, FileNotFoundException {
        players[currentPlayer].removeHand(card);
        Table.add(card);

        currentPlayer = (currentPlayer + 1) % players.length;

        if (currentPlayer == startPlayer) {
            currentPlayer = startPlayer = determineCycleWinner();
            while (!Table.isEmpty()) {
                players[startPlayer].addCollectedCard(Table.remove(0));
            }
            if (!deck.isEmpty()) {
                for (int i = 0; i < players.length; i++) {
                    players[(i + startPlayer) % players.length].addHand(deck.pop(),
                            (i + startPlayer) % players.length);
                }
            }
            HelloApplication.resetVisibility();
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
}
