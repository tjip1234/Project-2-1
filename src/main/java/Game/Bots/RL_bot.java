package Game.Bots;

import Cards.Card;
import Cards.Deck;

import java.util.ArrayList;

public class RL_bot extends Bot {
    private int briscolaValue = 5;
    private int amountofplayers = 2;
    Deck rememberdeck;
    public RL_bot() {
        Deck rememberdeck = new Deck();
    }
    @Override
    public Card MakeDecision(ArrayList<Card> cardsOnTable, Card.Suit Briscola) {
        rememberdeck.removeAll(playedCards);
        if (cardsOnTable.size() == amountofplayers-1){
            return NotMyTurn.MakeDecision(cardsOnTable,Briscola,rememberdeck,getHand());
        }
        int max = -999;
        Card bigcard = null;
        for(Card card: getHand()){
            if (getStrongness(card,Briscola) > max) {
                bigcard = card;
                max = getStrongness(card, Briscola);
            }
        }
        return bigcard;
    }
    public int getStrongness(Card card, Card.Suit briscola){
        if (card.suit == briscola)
            return card.number.scoreValue + card.number.ordinal() + briscolaValue;
        return card.number.scoreValue + card.number.ordinal();
    }

}
