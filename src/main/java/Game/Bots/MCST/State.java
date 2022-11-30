package Game.Bots.MCST;

import Game.Cards.Card;
import Game.GameSession;

import java.util.*;

public class State {
    private GameSession boardState;
    private Card cardPlayed;
    private static final Random random = new Random();

    private int player;

    public final int rootPlayerNumber;
    private int visitCountForState;
    private int scoreForState;

    private List<State> possibleStates;

    public State(GameSession board, int rootPlayerNumber) {
        this.rootPlayerNumber = rootPlayerNumber;
        this.boardState = board;
        for(int i = 0; i < this.boardState.players.length; ++i){
            if(i == rootPlayerNumber) continue;

            var hand = this.boardState.players[i].getHand();
            for(int j = hand.size() - 1; j >= 0; --j) {
                int deckI = this.boardState.deck.size() == 0 ? 0 : random.nextInt(0, this.boardState.deck.size());
                this.boardState.deck.add(deckI, hand.get(j));
                this.boardState.players[i].removeHand(hand.get(j));
            }
        }
    }

    //TODO are we taking into account rounds properly?
    public List<State> createAllPossibleStates() {
        // Generate possibilities if we haven't computed it yet
        if (possibleStates == null) {
            possibleStates = new ArrayList<>();
            if (boardState.currentPlayer == rootPlayerNumber) {
                var hand = boardState.players[boardState.currentPlayer].getHand();
                for (Card card : hand) {
                    GameSession tmp = boardState.clone();
                    tmp.playTurn(card);
                    State tempState = new State(tmp, rootPlayerNumber);
                    tempState.cardPlayed = card;
                    possibleStates.add(tempState);
                }
            }
            // Current player is not "me", create a node for every remaining card
            else {
                var remainingCards = new HashSet<Card>(boardState.deck.getSessionCards());
                remainingCards.removeAll(boardState.getPlayedCards());
                boardState.players[rootPlayerNumber].getHand().forEach(remainingCards::remove);

                for (Card card : remainingCards) {
                    GameSession tmp = boardState.clone();
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
        int randomChild = random.nextInt(0, getPossibleStates().size());

        State state = getPossibleStates().get(randomChild);
        state.createAllPossibleStates();

        return state;
    }

    public List<State> getPossibleStates() {
        return possibleStates;
    }

    public State getFittestChildState(int iterationCount){
        double chance = 1/(iterationCount+0.1);
        double roll = Math.random()/10;
        if(chance<roll){
            return getPossibleStates().stream().max(Comparator.comparingInt(a -> a.scoreForState/(a.visitCountForState+1))).get();
           // return getAllPossibleStates().stream().max(Comparator.comparingInt(a -> a.boardState.players[rootPlayerNumber].Score())).get();
        }else{
            return getRandomChildState();
        }

    }

    public void addToVisitCount() {
        this.visitCountForState++;
    }

    public void addWinScore(double score) {
        this.scoreForState += score;
    }

    public GameSession getBoardState() {
        return boardState;
    }

    public double getScoreForState() {
        return scoreForState;
    }

    public void setScoreForState(int scoreForState) {
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

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public int getRootPlayerNumber() {
        return rootPlayerNumber;
    }
}