package Game.Bots;

import Cards.Card;
import Cards.Deck;

import java.util.ArrayList;

public class NotMyTurn {
    public static Card MakeDecision(ArrayList<Card> cardsOnTable, Card.Suit Briscola, Deck currentcards,ArrayList<Card> hand) {
        cardsOnTable.sort(null);
        ArrayList<Card> winnables = new ArrayList<Card>();
        for (Card card : hand) {
            if ( card.compareTo(cardsOnTable.get(cardsOnTable.size()-1), Briscola) > 0)
                winnables.add(card);
        }
        switch (winnables.size()){
            case 0:
                hand.sort(null);
                return hand.get(0);
            default:
                winnables.sort(null);
                return winnables.get(0);
        }
    }

}
