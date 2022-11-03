package Game;

import Game.Cards.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player implements Cloneable {
    private ArrayList<Card> Hand;
    private ArrayList<Card> CollectedCards;

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

    public List<Card> getHand() {
        return Collections.unmodifiableList(Hand);
    }

    public void removeHand(Card card) {
        Hand.remove(card);
    }

    public void addCollectedCard(Card card) {
        CollectedCards.add(card);
    }
    @Override
    public Player clone() {
        try {
            Player clone = (Player) super.clone();
            clone.Hand = new ArrayList<>();
            clone.CollectedCards = new ArrayList<>();
            clone.Hand.addAll(Hand);
            clone.CollectedCards.addAll(CollectedCards);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
