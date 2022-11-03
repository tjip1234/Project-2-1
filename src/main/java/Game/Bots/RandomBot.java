package Game.Bots;

import Game.Cards.Card;

import java.util.List;
import java.util.Random;

public class RandomBot extends Bot{
    private final Random RNG = new Random();

    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) {
        var hand = getHand();
        return hand.get(RNG.nextInt(hand.size()));
    }
}
