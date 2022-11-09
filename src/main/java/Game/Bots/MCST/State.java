package Game.Bots.MCST;

import Game.Bots.Trees.Node;
import Game.Cards.Card;
import Game.GameSession;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class State {
    private GameSession board;
    public Card cardPlayed;
    private static final Random random = new Random();

    private int player;

    private final int rootPlayerNumber;
    private int visitCountForState;
    private double scoreForState;

    private List<State> possibleStates;

    public State(GameSession state, int rootPlayerNumber) {
        this.rootPlayerNumber = rootPlayerNumber;
        board = state;
    }

    //TODO are we taking into account rounds properly?
    public List<State> getAllPossibleStates() {
        // Generate possibilities if we haven't computed it yet
        if (possibleStates == null) {
            possibleStates = new ArrayList<>();
            if (board.currentPlayer == rootPlayerNumber) {
                var hand = board.players[board.currentPlayer].getHand();
                for (Card card : hand) {
                    GameSession tmp = board.clone();
                    tmp.playTurn(card);
                    State tempState = new State(tmp, rootPlayerNumber);
                    tempState.cardPlayed = card;
                    possibleStates.add(tempState);
                }
            }
            // Current player is not "me", create a node for every remaining card
            else {
                var remainingCards = new HashSet<Card>();
                remainingCards.addAll(board.deck.getSessionCards());
                remainingCards.removeAll(board.getPlayedCards());
                remainingCards.removeAll(board.players[rootPlayerNumber].getHand());

                for (Card card : remainingCards) {
                    GameSession tmp = board.clone();
                    tmp.playTurn(card);
                    State tempState = new State(tmp, rootPlayerNumber);
                    tempState.cardPlayed = card;
                    possibleStates.add(tempState);
                }
            }
        }

        return possibleStates;
    }

    public State getRandomChildState() {
        int randomChild = random.nextInt(0, getAllPossibleStates().size());
        return getAllPossibleStates().get(randomChild);
    }

    public void addToVisitCount() {
        this.visitCountForState++;
    }

    public void addWinScore(int score) {
        this.scoreForState += score;
    }

    public GameSession getBoard() {
        return board;
    }

    public double getScoreForState() {
        return scoreForState;
    }

    public void setScoreForState(double scoreForState) {
        this.scoreForState = scoreForState;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    //1 is the bot, 2 is opponent, needs to be changed for multiplayer
    public int getOpponent() {
        return (player % 2) + 1;
    }

    public int getVisitCountForState() {
        return visitCountForState;
    }

    public void setVisitCountForState(int visitCountForState) {
        this.visitCountForState = visitCountForState;
    }
}