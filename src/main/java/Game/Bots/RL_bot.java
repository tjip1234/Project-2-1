package Game.Bots;

import Game.Cards.Card;
import Game.Cards.Deck;

import java.util.List;

public class RL_bot extends Bot {
    private int briscolaValue = 5;
    private int amountofplayers = 2;
    Deck rememberdeck;
    public RL_bot() {
        rememberdeck = new Deck();
    }
    @Override
    public Card MakeDecision(List<Card> cardsOnTable, Card.Suit Briscola) {
        for (var p:playedCards
             ) {
            rememberdeck.remove(p);
        }
       /////// rememberdeck.removeAll(playedCards);
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
