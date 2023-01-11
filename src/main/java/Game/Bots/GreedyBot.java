package Game.Bots;

import Game.Cards.Card;
import Game.Cards.Card.Suit;

import java.util.Comparator;
import java.util.List;

public class GreedyBot extends Bot {
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Suit Briscola) {
        // Find the most valuable card on the table, may be null
        /*System.out.println();*/
        var dominantCard = FindDominantCard(cardsOnTable, Briscola);

        Comparator<Card> comparator = (Card lhs, Card rhs) -> dominantCard == null? lhs.compareTo(rhs, Briscola) : lhs.compareTo(rhs, Briscola,dominantCard.suit);

/*
        System.out.println("\nGREEDY CARD PreSORT");
        for(var card :getHand())
            System.out.println(card);
*/


        // Sort the cards based on relative value against the current dominant card
        var sortedCards = getHand().stream().sorted(comparator).toList();

/*
        System.out.println("\nGREEDY CARD SORT");
        for(var card :sortedCards)
            System.out.println(card);
*/

        // Find the smallest card that we can ue to become dominant, if available
        for (var card : sortedCards)
            if (comparator.compare(card, dominantCard) > 0)
                return card;

        // If we have no card to overpower, we instead use our worst card to reduce damages
        return sortedCards.get(0);
    }
}
