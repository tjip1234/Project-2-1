package Game.Bots;

import Cards.Card;

import java.util.ArrayList;
import java.util.Random;

public class RandomBot extends Bot{
    private final static Random RNG = new Random();

    @Override
    public Card MakeDecision(ArrayList<Card> cardsOnTable, Card.Suit Briscola) {
        var hand = getHand();
        return hand.get(RNG.nextInt(hand.size()));
    }
}
