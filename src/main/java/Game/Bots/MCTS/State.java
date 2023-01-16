package Game.Bots.MCTS;

import Game.Cards.Card;
import Game.GameSession;

import java.util.*;

public class State {
    private GameSession boardState;
    private Card cardPlayed;
    private static final Random random = new Random();


    public final int rootPlayerNumber;
    private int visitCountForState;
    private double scoreForState;

    private List<State> possibleStates;
    private List<Card> remainingCards = new ArrayList<>();

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
    public void createAllPossibleStates() {
        // Generate possibilities if we haven't computed it yet
        possibleStates = new ArrayList<>();
        if (boardState.currentPlayer == rootPlayerNumber) {
            remainingCards = new ArrayList<>((boardState.players[boardState.currentPlayer].getHand()));
        }
        // Current player is not "me", create a node for every remaining card
        else {
            var remainingCardsTemp = new HashSet<>(boardState.deck.getSessionCards());
            remainingCardsTemp.removeAll(boardState.getPlayedCards());
            boardState.players[rootPlayerNumber].getHand().forEach(remainingCardsTemp::remove);
            remainingCards = new ArrayList<>(remainingCardsTemp);

        }

    }

//    public List<State> createAllPossibleStates2() {
//        // Generate possibilities if we haven't computed it yet
//        if (possibleStates == null) {
//            possibleStates = new ArrayList<>();
//            if (boardState.currentPlayer == rootPlayerNumber) {
//                var remainingCards = boardState.players[boardState.currentPlayer].getHand();
//                for (Card card : hand) {
//                    GameSession tmp = boardState.clone();
//                    tmp.playTurn(card);
//                    State tempState = new State(tmp, rootPlayerNumber);
//                    tempState.cardPlayed = card;
//                    possibleStates.add(tempState);
//                }
//            }
//            // Current player is not "me", create a node for every remaining card
//            else {
//                remainingCards = new HashSet<Card>(boardState.deck.getSessionCards());
//                remainingCards.removeAll(boardState.getPlayedCards());
//                boardState.players[rootPlayerNumber].getHand().forEach(remainingCards::remove);
//
//                for (Card card : remainingCards) {
//                    GameSession tmp = boardState.clone();
//                    tmp.playTurn(card);
//                    State tempState = new State(tmp, rootPlayerNumber);
//                    tempState.cardPlayed = card;
//                    possibleStates.add(tempState);
//                }
//            }
//        }
//
//        return possibleStates;
//    }


    public boolean canYouAddAnChildState(){
        return remainingCards.size()>0;
    }
    public State getRandomChildState() {

        int randomChild = random.nextInt(0, remainingCards.size());
        GameSession tmp = boardState.clone();
        Card holder = remainingCards.remove(randomChild);
        tmp.playTurn(holder);
        State state = new State(tmp, rootPlayerNumber);
        state.cardPlayed = holder;
        state.createAllPossibleStates();
        return state;
    }

    public State getRandomChildStateSimulated() {

        int randomChild = random.nextInt(0, remainingCards.size()+possibleStates.size());
        GameSession tmp = boardState.clone();
        State state;
        if(randomChild<=remainingCards.size()||possibleStates.size()==0){

            Card holder = remainingCards.get(random.nextInt(remainingCards.size()));
            tmp.playTurn(holder);
            state = new State(tmp, rootPlayerNumber);
        }else {
            state = possibleStates.get(random.nextInt(possibleStates.size()));


        }
        possibleStates.add(state);
        state.createAllPossibleStates();

        return state;
    }

    public List<Card> getPossibleUncheckedStates() {
        return remainingCards;
    }
    public List<State> getPossibleCheckedStates() {
        return possibleStates;
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