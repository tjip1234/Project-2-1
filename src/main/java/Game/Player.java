package Game;

import Cards.Card;

import java.util.ArrayList;

public class Player {
    ArrayList<Card> Hand;
    ArrayList<Card> CollectedCards;

    public Player() {
        Hand = new ArrayList<>();
        CollectedCards = new ArrayList<>();
    }

    public int Score() {
        int score = 0;
        for (int i = 0; i < CollectedCards.size(); i++)
            score += CollectedCards.get(i).number.scoreValue;

        return score;
    }

    public void addHand(Card card) {
        Hand.add(card);
    }

    public ArrayList<Card> getHand() {
        return Hand;
    }

    public void removeHand(Card card) {
        Hand.remove(card);
    }

    public void addCollectedCard(Card card) {
        CollectedCards.add(card);
    }
}
