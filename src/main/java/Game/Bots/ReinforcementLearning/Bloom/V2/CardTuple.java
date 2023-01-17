package Game.Bots.ReinforcementLearning.Bloom.V2;

import Game.Cards.Card;

import java.io.Serializable;

public record CardTuple(int suitOrdinal, int numberOrdinal) implements Serializable {
    public static CardTuple fromCard(Card card){
        if(card == null)
            return null;

        return new CardTuple(card.suit.ordinal(), card.number.ordinal());
    }

    public Card toCard(){
        return new Card(Card.suitVal[suitOrdinal], Card.numberVals[numberOrdinal]);
    }
}
