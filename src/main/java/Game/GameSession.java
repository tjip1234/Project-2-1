package Game;

import Cards.Card;
import Cards.Deck;

import java.util.ArrayList;

public class GameSession {
    public ArrayList<Player> players;
    public ArrayList<Card> Table = new ArrayList<>();
    public int currentPlayer; // TODO whatever public stuff
    int startPlayer = 0;

    public Deck deck;

    public GameSession(int AmountOfPlayers) {
        players = new ArrayList<Player>();
        currentPlayer = 0;
        deck = new Deck();

        for (int i = 0; i < AmountOfPlayers; i++) {
            players.add(new Player());
        }
    }

    public void startRound() {
        deck.reshuffle();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < players.size(); i++) {
                players.get((i + startPlayer) % players.size()).addHand(deck.pop());
            }
        }
    }

    public void playTurn(Card card) {
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
                    players.get((i + startPlayer) % players.size()).addHand(deck.pop());
                }
            }
            if (gameOver()) {
                int playerWinner = 0;
                for (int i = 1; i < players.size(); i++) {
                    if (players.get(i).Score() > players.get(playerWinner).Score())
                        playerWinner = i;
                    // TODO if the score is equal
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

    private boolean gameOver() {
        return players.get(0).getHand().isEmpty();
    }
}
