package Game.Bots;

import Cards.Card;
import Cards.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    public Card getCard(int chosenCard){
        return hand.get(chosenCard);
    }
    public void playTurn(int player, Card card){
        /*TODO Play a card and change the board accordingly
        change hand correctly
        change field
        change playedCards
        */
        if(player==1){
            this.getHand().remove(card);
        }

    }


    public Stack<Card> getPossibleCards() {
        Deck cardsLeft = new Deck(40);
        for(Card card:playedCards) {
            cardsLeft.remove(card);
        }
        return cardsLeft;
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

    public boolean checkFinished() {
        return playedCards.size() == 40;
    }
}
