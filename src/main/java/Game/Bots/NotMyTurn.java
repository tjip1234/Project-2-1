package Game.Bots;

import Game.Cards.Card;
import Game.Cards.Deck;

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
        //stay of this function shouldn't use compare to because suit doesn't have to be the same
        if (winnables.size() == 0) {
            // Get the card with the minimum value in our hand
            Card chosenOne = hand.get(0);
            for (int i = 1; i < hand.size(); i++) {
                if (hand.get(i).number.scoreValue < chosenOne.number.scoreValue || ((hand.get(i).number.scoreValue <= chosenOne.number.scoreValue) &&(hand.get(i).number.ordinal() < chosenOne.number.ordinal())))
                    chosenOne = hand.get(i);

            }
            return chosenOne;
        }

        // Get the lowest valued card we can use to potentially win this match.
        return winnables.stream().min(Card::compareTo).get();
    }

}
