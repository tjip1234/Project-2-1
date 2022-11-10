package Game.Bots;

import java.util.List;

import Game.Cards.Card;
import Game.Cards.Card.Suit;

public class GreedyBot extends Bot {
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Suit Briscola) {
        var dominantCard = FindDominantCard(cardsOnTable, Briscola);

        var sortedCards = cardsOnTable.stream().sorted((lhs, rhs) -> lhs.compareTo(rhs, Briscola)).toList();

        for (var card : sortedCards)
            if (card.compareTo(dominantCard, Briscola) > 0)
                return card;

        return sortedCards.get(0);
    }
}
