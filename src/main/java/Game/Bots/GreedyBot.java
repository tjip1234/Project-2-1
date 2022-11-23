package Game.Bots;

import java.util.List;

import Game.Cards.Card;
import Game.Cards.Card.Suit;

public class GreedyBot extends Bot {
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Suit Briscola) {
        // Find the most valuable card on the table, may be null
        var dominantCard = FindDominantCard(cardsOnTable, Briscola);

        // Sort the cards based on relative value against the current dominant card
        var sortedCards = getHand().stream().sorted((lhs, rhs) -> lhs.compareTo(rhs, Briscola)).toList();

        // Find the smallest card that we can use to become dominant, if available
        for (var card : sortedCards)
            if (card.compareTo(dominantCard, Briscola) > 0)
                return card;

        // If we have no card to overpower, we instead use our worst card to reduce damages
        return sortedCards.get(0);
    }
}
