package Game.Bots;

import Cards.Card;
import Cards.Deck;

import java.util.ArrayList;
import java.util.List;

public class NotMyTurn {
    public static Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola, Deck currentcards, List<Card> hand) {
        // We are using streams because the parameters are unmodifiable for safety reasons
        // This applies below as well.
        var sortedCards = cardsOnTable.stream().sorted();

        ArrayList<Card> winnables = new ArrayList<Card>();
        for (Card card : hand) {
            if ( card.compareTo(cardsOnTable.get(cardsOnTable.size()-1), Briscola) > 0)
                winnables.add(card);
        }
        if (winnables.size() == 0) {
            // Get the card with the minimum value in our hand
            return hand.stream().min(Card::compareTo).get();
        }

        // Get the lowest valued card we can use to potentially win this match.
        return winnables.stream().min(Card::compareTo).get();
    }

}
