package Game.Bots;

import Cards.Card;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Card> hand;
    private List<Card> fieldCards;
    private List<Card> playedCards;
    public Board(){
        this.hand = new ArrayList<>();
        this.fieldCards = new ArrayList<>();
        this.playedCards = new ArrayList<>();
    }
    public Board(Board board){
        this.hand = board.getHand();
        this.playedCards = board.getPlayedCards();
        this.fieldCards = board.getFieldCards();
    }

    //TODO needs to be changed to remove perhaps
    public Card getCard(int chosenCard){
        return hand.get(chosenCard);
    }
    public void playTurn(int player, Card card){
        /*TODO Play a card and change the board accordingly
        change hand correctly
        change field
        change playedCards
        */
    }


    public List<Card> getPossibleCards() {
        //TODO return a list of cards that has not been played yet
        return null;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }

    public List<Card> getFieldCards() {
        return fieldCards;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setFieldCards(List<Card> fieldCards) {
        this.fieldCards = fieldCards;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public void setPlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }
}
