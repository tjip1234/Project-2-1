package Game;

import Cards.Card;
import Cards.Deck;
import com.example.project_2.HelloApplication;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class GameSession {
    public ArrayList<Player> players;
    public ArrayList<Card> Table = new ArrayList<>();
    public int currentPlayer;
    int startPlayer = 0;

    public final boolean isTeamGame;

    public Deck deck;

    public GameSession(int AmountOfPlayers) {
        if (AmountOfPlayers <= 0)
            throw new UnsupportedOperationException("Games don't work like that. Dumbass.");
        if (AmountOfPlayers == 1)
            throw new UnsupportedOperationException(
                    "Trying to play this game by yourself? How sad... Go get some friends you pathetic piece of shit loser");

        if (AmountOfPlayers == 5)
            throw new UnsupportedOperationException("Briscola Chiamata is unsupported.");

        if (AmountOfPlayers > 6)
            throw new UnsupportedOperationException(
                    "As fun as that may sound, you can't play briscola with that many players. The limit is 6.");

        players = new ArrayList<Player>();
        currentPlayer = 0;

        for (int i = 0; i < AmountOfPlayers; i++) {
            players.add(new Player());
        }

        isTeamGame = AmountOfPlayers != 2 && AmountOfPlayers % 2 == 0;

        if (AmountOfPlayers == 3)
            deck = new Deck(39);
        else if (AmountOfPlayers == 6)
            deck = new Deck(36);
        else
            deck = new Deck(40);
    }

    public void startRound() {
        deck.reshuffle();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < players.size(); i++) {
                players.get((i + startPlayer) % players.size()).addHand(deck.pop(),(i + startPlayer) % players.size());
            }
        }
    }

    public void playTurn(Card card) throws MalformedURLException, FileNotFoundException {
        players.get(currentPlayer).removeHand(card);
        Table.add(card);

        currentPlayer = (currentPlayer + 1) % players.size();

        if (currentPlayer == startPlayer) {
            currentPlayer = startPlayer = determineCycleWinner();
            while (!Table.isEmpty()) {
                players.get(startPlayer).addCollectedCard(Table.remove(0));
            }
            if (!deck.isEmpty()) {
                for (int i = 0; i < players.size(); i++) {
                    players.get((i + startPlayer) % players.size()).addHand(deck.pop(), (i + startPlayer) % players.size());
                }
            }
            HelloApplication.resetVisibility();
            if (gameOver()) {
                int playerWinner = 0;
                for (int i = 1; i < players.size(); i++) {
                    if (players.get(i).Score() > players.get(playerWinner).Score())
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

        return (winner + startPlayer) % players.size();
    }

    public boolean gameOver() {
        return players.get(currentPlayer).getHand().isEmpty();
    }

    public Integer getScoreForTeam(int teamNumber) {
        if (!isTeamGame)
            return 0;

        int total = 0;

        for (int i = teamNumber; i < players.size(); i += 2)
            total += players.get(i).Score();

        return total;
    }

    public Player getPlayer(int playerNumber) {
        return players.get(playerNumber % players.size());
    }

    public int getTeamNumber(int playerNumber) {
        return playerNumber % 2;
    }
}
